package com.flowdock.jenkins;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import java.io.UnsupportedEncodingException;

public class ChatMessage extends FlowdockMessage {
    protected String externalUserName;

    public ChatMessage() {
        this.externalUserName = "Jenkins";
    }

    public void setExternalUserName(String externalUserName) {
        this.externalUserName = externalUserName;
    }

    public String asPostData() throws UnsupportedEncodingException {
        StringBuffer postData = new StringBuffer();
        postData.append("content=").append(urlEncode(content));
        postData.append("&external_user_name=").append(urlEncode(externalUserName));
        postData.append("&tags=").append(urlEncode(tags));
        return postData.toString();
    }

    public static ChatMessage fromBuild(AbstractBuild build) {
        ChatMessage msg = new ChatMessage();
        StringBuffer content = new StringBuffer();
        String displayName = build.getDisplayName().replaceAll("#", "No. ");
        content.append("Build: ").append(build.getProject().getName()).
            append(" ").append(displayName).
            append(" is ").append(build.getResult().toString());

        String rootUrl = Hudson.getInstance().getRootUrl();
        String buildLink = (rootUrl == null) ? null : rootUrl + build.getUrl();
        if(buildLink != null) content.append(" \n").append(buildLink);

        msg.setContent(content.toString());
        return msg;
    }
}
