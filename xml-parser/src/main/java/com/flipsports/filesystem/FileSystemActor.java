package com.flipsports.filesystem;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.flipsports.filesystem.FileMonitorMsg.FileCreated;
import com.flipsports.utils.ActorLookupSupport;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

import static com.flipsports.SystemConfig.ACTOR_CONSUMER;

@Slf4j
public class FileSystemActor extends AbstractActor implements ActorLookupSupport {
    public static Props props(Path path) {
        return Props.create(FileSystemActor.class, () -> new FileSystemActor(path));
    }

    private final WatchServiceTask watchServiceTask;
    private final Thread watchThread;


    public FileSystemActor(Path pathToMonitor) throws IOException {
        this.watchServiceTask = new WatchServiceTask(pathToMonitor, this.getSelf());
        this.watchThread = new Thread(watchServiceTask, "WatchService");
        watchServiceTask.watch();
    }

    @Override
    public void preStart() throws Exception {
        watchThread.setDaemon(true);
        watchThread.start();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FileCreated.class, this::fileCreated)
                .build();
    }

    private void fileCreated(FileCreated fileCreated) {
        log.info("New file uploaded: " + fileCreated.getFile());
        getContext().actorFor(top(ACTOR_CONSUMER)).tell(fileCreated, this.getSelf());
    }

    @Override
    public void postStop() throws Exception {
        watchThread.interrupt();
    }
}