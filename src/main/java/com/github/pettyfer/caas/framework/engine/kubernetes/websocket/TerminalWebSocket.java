package com.github.pettyfer.caas.framework.engine.kubernetes.websocket;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.framework.engine.kubernetes.session.TerminalSession;
import com.github.pettyfer.caas.framework.engine.kubernetes.utils.BlockingInputStreamPumper;
import com.github.pettyfer.caas.framework.engine.kubernetes.utils.SessionStorage;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Callback;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author Pettyfer
 */
@Slf4j
@ServerEndpoint(value = ApiConstant.API_V1_PREFIX + "/sockjs/terminal/{namespaceId}/{name}")
@Component
public class TerminalWebSocket {

    private static KubernetesClient kubernetesClient;

    private static IBizNamespaceService bizNamespaceService;

    @Autowired
    public void setKubernetesClient(KubernetesClient kubernetesClient) {
        TerminalWebSocket.kubernetesClient = kubernetesClient;
    }

    @Autowired
    public void setBizNamespaceService(IBizNamespaceService bizNamespaceService) {
        TerminalWebSocket.bizNamespaceService = bizNamespaceService;
    }

    private static final ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
            20, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("terminal-process-", false));

    @OnOpen
    public void onOpen(Session session, @PathParam("namespaceId") String namespaceId, @PathParam("name") String name) {
        try {
            Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
            if(namespaceOptional.isPresent()){
                PodResource<Pod> podResource = kubernetesClient.pods().inNamespace(namespaceOptional.get().getName().trim()).withName(name.trim());
                String[] validShells = new String[]{"bash", "sh", "powershell", "cmd"};
                String shell = "";
                for (String s : validShells) {
                    String execCommandOnPod = execCommandOnPod(podResource, s);
                    if ("".equals(execCommandOnPod)) {
                        shell = s;
                        break;
                    } else {
                        if (session.isOpen()) {
                            session.getBasicRemote().sendText(execCommandOnPod);
                        }
                    }
                }
                ExecWatch watch = podResource
                        .redirectingInput()
                        .redirectingOutput()
                        .redirectingError()
                        .redirectingErrorChannel()
                        .withTTY()
                        .exec(shell);
                TerminalSession terminalSession = new TerminalSession();
                terminalSession.setSession(session);
                terminalSession.setExecWatch(watch);
                SessionStorage.getInstance().addSession(terminalSession);
            } else {
                throw new BaseRuntimeException("命名空间不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClose
    @SneakyThrows
    public void onClose(Session session) {
        SessionStorage.getInstance().removeSession(session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject json = JSON.parseObject(message);
        String type = json.getString("type");
        try {
            TerminalSession terminalSession = SessionStorage.getInstance().getSession(session.getId());
            switch (type) {
                case "init":
                case "resize":
                    terminalSession.getExecWatch().resize(json.getIntValue("cols"), json.getIntValue("rows"));
                    break;
                case "stdin":
                    terminalSession.getExecWatch().getInput().write(json.getString("data").getBytes());
                    break;
                default:
                    break;
            }
            executorService.execute(new BlockingInputStreamPumper(terminalSession.getExecWatch().getOutput(), new Callback<byte[]>() {
                @Override
                public void call(byte[] input) {
                    try {
                        SessionStorage.getInstance().sendMessage(session.getId(), new String(input));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
            executorService.execute(new BlockingInputStreamPumper(terminalSession.getExecWatch().getError(), new Callback<byte[]>() {
                @Override
                public void call(byte[] input) {
                    try {
                        SessionStorage.getInstance().sendMessage(session.getId(), new String(input));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
            executorService.execute(new BlockingInputStreamPumper(terminalSession.getExecWatch().getErrorChannel(), new Callback<byte[]>() {
                @Override
                public void call(byte[] input) {
                    try {
                        SessionStorage.getInstance().sendMessage(session.getId(), new String(input));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    public String execCommandOnPod(PodResource<Pod> podResource, String... cmd) {
        CompletableFuture<String> data = new CompletableFuture<>();
        try (ExecWatch execWatch = execCmd(podResource, data, cmd)) {
            return data.get(10, TimeUnit.SECONDS);
        }

    }

    private ExecWatch execCmd(PodResource<Pod> podResource, CompletableFuture<String> data, String... command) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return podResource
                .writingOutput(baos)
                .writingError(baos)
                .usingListener(new ValidListener(data, baos))
                .exec(command);
    }

    static class ValidListener implements ExecListener {

        private CompletableFuture<String> data;
        private ByteArrayOutputStream baos;

        public ValidListener(CompletableFuture<String> data, ByteArrayOutputStream baos) {
            this.data = data;
            this.baos = baos;
        }

        @Override
        public void onOpen(Response response) {
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            data.completeExceptionally(t);
        }

        @Override
        public void onClose(int code, String reason) {
            data.complete(baos.toString());
        }
    }

}
