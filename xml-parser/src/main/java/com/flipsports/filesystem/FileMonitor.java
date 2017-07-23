package com.flipsports.filesystem;

import akka.actor.ActorSystem;
import com.flipsports.xml.XmlConsumerActor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.flipsports.SystemConfig.ACTOR_CONSUMER;
import static com.flipsports.SystemConfig.ACTOR_WATCHER;

@Slf4j
public class FileMonitor {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please set the path to monitor - only one parameter!");
            System.exit(1);
        }

        val path = Paths.get(args[0]);
        if (Files.notExists(path)) {
            System.out.println("Please set existing path");
            System.exit(1);
        }

        val system = ActorSystem.create("FileMonitor");
        try {
            log.info("Watching for files upload into dir: {}", path.toString());
            system.actorOf(FileSystemActor.props(path), ACTOR_WATCHER);
            system.actorOf(XmlConsumerActor.props(), ACTOR_CONSUMER);

            log.info("Press ENTER to stop file watcher...");
            System.in.read();
        } finally {
            log.info("Terminating akka: {}", system.toString());
            system.terminate();
        }
    }

    //akka://my-sys/user/service-a/worker1


}
