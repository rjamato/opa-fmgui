package com.intel.stl.api.configuration;

import java.io.Serializable;

import org.w3c.dom.Node;

public abstract class AppenderConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String conversionPattern;

    private LoggingThreshold threshold;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the conversionPattern
     */
    public String getConversionPattern() {
        return conversionPattern;
    }

    /**
     * @param conversionPattern
     *            the conversionPattern to set
     */
    public void setConversionPattern(String conversionPattern) {
        this.conversionPattern = conversionPattern;
    }

    /**
     * @return the thresholdValue
     */
    public LoggingThreshold getThreshold() {
        return threshold;
    }

    /**
     * @param thresholdValue
     *            the thresholdValue to set
     */
    public void setThreshold(LoggingThreshold threshold) {
        this.threshold = threshold;
    }

    public abstract void updateNode(Node node, ILogConfigFactory factory);

    public abstract Node createNode(ILogConfigFactory factory);

    public abstract void populateFromNode(Node node, ILogConfigFactory factory);
}
