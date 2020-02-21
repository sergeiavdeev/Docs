package com.avdeev.docs.core.network.pojo;

public class AuthRequest {
   private boolean success;

   public AuthRequest() {
       success = false;
   }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
