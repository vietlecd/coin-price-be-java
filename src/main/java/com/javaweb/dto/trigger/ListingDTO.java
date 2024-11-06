package com.javaweb.dto.trigger;

public class ListingDTO {
    private String symbol;
    private String notificationMethod;
    private boolean shouldNotify;


    public ListingDTO() {}

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(String notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public boolean isShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }


    public static class Builder {
        private String symbol;
        private String notificationMethod;
        private boolean shouldNotify;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setNotificationMethod(String notificationMethod) {
            this.notificationMethod = notificationMethod;
            return this;
        }

        public Builder setShouldNotify(boolean shouldNotify) {
            this.shouldNotify = shouldNotify;
            return this;
        }

        public ListingDTO build() {
            ListingDTO listingDTO = new ListingDTO();
            listingDTO.setSymbol(this.symbol);
            listingDTO.setNotificationMethod(this.notificationMethod);
            listingDTO.setShouldNotify(this.shouldNotify);
            return listingDTO;
        }
    }
}
