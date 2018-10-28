package com.sourcey.materiallogindemo;

import java.util.List;

public class SignupResponseDTO {

    private String status;
    private String error;
    private List<Valid> valid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Valid> getValid() {
        return valid;
    }

    public void setValid(List<Valid> valid) {
        this.valid = valid;
    }

    public class Valid {
        private String field;
        private String message;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /*
    {
      "status": 400,
      "error": "validation-error",
      "valid": [
        {
          "field": "password",
          "message": "size must be between 8 and 500"
        }
      ]
    }
    * */
}
