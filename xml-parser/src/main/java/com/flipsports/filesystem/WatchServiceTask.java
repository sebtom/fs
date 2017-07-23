package com.flipsports.filesystem;

import akka.actor.ActorRef;
import com.flipsports.filesystem.FileMonitorMsg.FileCreated;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;

import static akka.actor.ActorRef.noSender;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Slf4j
public class WatchServiceTask implements Runnable {
    private final Path pathToMonitor;
    private final ActorRef notifyActor;

    private final WatchService watchService;

    public WatchServiceTask(Path pathToMonitor, ActorRef notifyActor) throws IOException {
        this.pathToMonitor = pathToMonitor;
        this.notifyActor = notifyActor;
        this.watchService = FileSystems.getDefault().newWatchService();
    }

    public void watch() throws IOException {
        pathToMonitor.register(watchService, ENTRY_CREATE);
    }

    @Override
    public void run() {
        try {
            log.info("Waiting for file system events...");
            while (!Thread.currentThread().isInterrupted()) {
                val key = watchService.take();
                key.pollEvents().forEach((event) -> {

                    val relativePath = (Path) event.context();
                    val path = ((Path) key.watchable()).resolve(relativePath);

                    switch (event.kind().name()) {
                        case "ENTRY_CREATE":
                            notifyActor.tell(new FileCreated(path.toFile()), noSender());
                            break;
                        default:
                            log.warn("Unknown event: {}", event.kind().name());
                    }
                });

                key.reset();
            }
        } catch (InterruptedException e) {
            log.info("Interrupting watchService!");
        } finally {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("Could not close watchService!", e);
            }
        }
    }
}