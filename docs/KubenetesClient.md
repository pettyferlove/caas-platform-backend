# 简介
由于Kubenetes远程dashboard用户体验不友好，大部分使用yaml文件进行部署，因此进行此项目，通过封装api对外提供友好的接口，并使用可视化界面予以控制

# 目标
基于官方kubernetes-client/java类库，实现通过java完成对Kubenetes原生资源对象（pod、node、namespace、servcie、deployment）和自定义资源对象（如：cluster）的增删改查或事件监听（watch）

# 开源框架
## 开源框架介绍
> 目前通过java操作k8s，开源版本共有两个：
> * [kubernetes-client/java](https://github.com/kubernetes-client/java)
> * [fabric8io/kubernetes-client](https://github.com/fabric8io/kubernetes-client)

## 对比
名称  | 和官网API一致性 | 社区活跃度 |  代码生成
--- | --- | --- | ---
kubernetes-client/java | 根据k8s-openapi随之更新，一致性和更新频率高 | 目前不活跃 | kubernetes-client/java提供了生成代码的通用跨语言工具，该工具托管在 kubernetes-client / gen存储库中
fabric8io/kubernetes-client | 一致性低，更新慢；其中不支持k8s1.8和1.13 | 社区活跃，目前使用者多 | 暂无

> 由于kubernetes-client/java社区活跃度低，且实际开发过程中由于和google swagger出入导致调用失败的情况比较多，故此选择社区活跃度高且稳定性较高的fabric8io/kubernetes-client