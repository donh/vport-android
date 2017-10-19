package com.a21vianet.wallet.vport.library.commom.http.ipfs.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class UserInfoIPFS {

    @SerializedName("@context")
    private String context = "http://schema.org"; // FIXME check this code
    @SerializedName("@type")
    private String type = "Person"; // FIXME check this code
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description = "";
    @SerializedName("address")
    private String address;
    @SerializedName("network")
    private String network = "vChain";
    @SerializedName("publicKey")
    private String publicKey;
    @SerializedName("image")
    private ImageBean image;

    public UserInfoIPFS() {
    }

    public UserInfoIPFS(String name, String address, String publicKey) {
        this.name = name;
        this.address = address;
        this.publicKey = publicKey;
    }

    public UserInfoIPFS(String context, String type, String name, String
            description, String address, String network, String publicKey, ImageBean image) {
        this.context = context;
        this.type = type;
        this.name = name;
        this.description = description;
        this.address = address;
        this.network = network;
        this.publicKey = publicKey;
        this.image = image;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public static class ImageBean {
        @SerializedName("@type")
        private String type = "ImageObject"; // FIXME check this code
        @SerializedName("name")
        private String name = "avatar";
        @SerializedName("contentUrl")
        private String contentUrl;

        public String getType() {
            return type;
        }

        public void setType(String Type) {
            this.type = Type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }
    }
}
