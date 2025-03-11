package kd.cus.api.PUBConfigUtils;

import com.google.gson.Gson;

public class SpicRabbitMQConfig {
    private String RabbitMQHost;
    private String RabbitMQPort;
    private String Username;
    private String Password;
    private String VirtualHost;
    private String Exchange;
    private String ExchangeType;
    private String QueneName;
    private String SystemCode;

    static { }

    public static SpicRabbitMQConfig getSpicRabbitMQConfig(){
        Gson gson = new Gson();
        SpicRabbitMQConfig _this = gson.fromJson(System.getProperty("spic_RabbitMQ"), SpicRabbitMQConfig.class);
        return _this;
    }

    public String getRabbitMQHost() {
        return this.RabbitMQHost;
    }

    public String getRabbitMQPort() {
        return this.RabbitMQPort;
    }

    public String getUsername() {
        return this.Username;
    }

    public String getPassword() {
        return this.Password;
    }

    public String getVirtualHost() {
        return this.VirtualHost;
    }

    public String getExchange() {
        return this.Exchange;
    }

    public String getExchangeType() {
        return this.ExchangeType;
    }

    public String getQueneName() {
        return this.QueneName;
    }

    public String getSystemCode() {
        return this.SystemCode;
    }
}
