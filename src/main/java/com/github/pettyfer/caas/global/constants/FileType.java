package com.github.pettyfer.caas.global.constants;

public enum FileType {

    /**
     * JPEG
     */
    IMAGE_JPEG("image/jpeg", "image", ".jpeg"),
    IMAGE_JPG("image/jpg", "image", ".jpg"),
    IMAGE_PNG("image/png", "image", ".png"),
    GZIP("application/gzip", "tar.gz", ".tar.gz"),
    BUILD_STREAM("application/octet-stream", "tar.gz", ".tar.gz"),
    EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "documentation", ".xlsx");
    private String contentType;

    private String type;

    private String expansionName;

    FileType(String contentType, String type, String expansionName) {
        this.contentType = contentType;
        this.type = type;
        this.expansionName = expansionName;
    }

    @Override
    public String toString() {
        return contentType;
    }

    public String getExpansionName() {
        return expansionName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getType() {
        return type;
    }

    public boolean isImage() {
        return "image".equalsIgnoreCase(type);
    }

    public static FileType parse(String contentType) {
        for (FileType cacl : FileType.values()) {
            if (cacl.toString().equals(contentType)) {
                return cacl;
            }
        }
        throw new IllegalArgumentException("Unable to parse the provided type " + contentType);
    }
}
