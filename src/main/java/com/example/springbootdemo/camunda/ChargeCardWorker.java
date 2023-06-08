package com.example.springbootdemo.camunda;

import org.camunda.bpm.client.ExternalTaskClient;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;

/**
 * camunda demo
 *
 * @author chujunjie
 * @date create in 15:27 2023/6/7
 */
public class ChargeCardWorker {

    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                // long polling timeout
                .asyncResponseTimeout(10000)
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("charge-card")
                // the default lock duration is 20 seconds, but you can override this
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String item = externalTask.getVariable("item");
                    Integer amount = externalTask.getVariable("amount");

                    LOGGER.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");

                    try {
                        Desktop.getDesktop().browse(new URI("https://docs.camunda.org/get-started/quick-start/complete"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
