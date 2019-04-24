package com.rippleitt.modals;

/**
 * Created by manishautomatic on 04/03/18.
 */

public class SliderItems {

    private String sliderOptionName;
    private Integer sliderDrawableResource;
    private boolean isMessage=false;

    public String getSliderOptionName() {
        return sliderOptionName;
    }

    public void setSliderOptionName(String sliderOptionName) {
        this.sliderOptionName = sliderOptionName;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public void setMessage(boolean message) {
        isMessage = message;
    }

    public Integer getSliderDrawableResource() {
        return sliderDrawableResource;
    }

    public void setSliderDrawableResource(Integer sliderDrawableResource) {
        this.sliderDrawableResource = sliderDrawableResource;
    }
}
