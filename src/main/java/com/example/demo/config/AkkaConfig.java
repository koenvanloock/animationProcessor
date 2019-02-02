package com.example.demo.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.demo.actors.PlaylistActor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class AkkaConfig {

  @Bean
  public ActorSystem actorSystem() {
    return ActorSystem.create("animations");
  }

  @Bean
  public DisposableBean disposableBean(ActorSystem actorSystem) {
    return () -> {
      CountDownLatch latch = new CountDownLatch(1);
      actorSystem.terminate();
      actorSystem.getWhenTerminated().whenComplete((a, b) -> latch.countDown());
      latch.await();
    };
  }

  @Bean
  public ActorRef playlistActor(ActorSystem actorSystem){
    return actorSystem.actorOf(Props.create(PlaylistActor.class));
  }
}
