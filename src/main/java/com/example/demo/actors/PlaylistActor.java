package com.example.demo.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import com.example.demo.models.*;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class PlaylistActor extends AbstractActor {

  private LinkedList<Animation> playlist;
  private ActorRef currentAnimationActor;

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create()
        .match(StartPlaylist.class, handleStartPlaylist())
        .match(ScheduleNextAnimation.class, handleScheduleNextAnimation)
        .match(StopPlaylist.class, stopPlaylist -> getContext().stop(currentAnimationActor))
    .build();
  }

  FI.UnitApply<ScheduleNextAnimation> handleScheduleNextAnimation = x -> {
    Animation current = playlist.pop();
    getContext().stop(currentAnimationActor);
    playlist.addLast(current);
    startFirstAnimationOfPlaylist(current);
    getContext().getSystem().scheduler().scheduleOnce(
        Duration.ofSeconds(current.getDurationInSeconds()),
        getSelf(),
        new ScheduleNextAnimation(),
        getContext().getDispatcher(),
        null);
  };

  private FI.UnitApply<StartPlaylist> handleStartPlaylist() {
    return startPlaylist -> {
      this.playlist = new LinkedList<>();
      this.playlist.addAll(startPlaylist.getAnimations());
      Animation current = this.playlist.pop();
      startFirstAnimationOfPlaylist(current);
      this.playlist.addLast(current);
      getContext().getSystem().scheduler().scheduleOnce(
          Duration.ofSeconds(current.getDurationInSeconds()),
          getSelf(),
          new ScheduleNextAnimation(),
          getContext().getDispatcher(),
          null);
    };
  }

  private void startFirstAnimationOfPlaylist(Animation animation) {
    currentAnimationActor = getContext().getSystem().actorOf(Props.create(AnimationActor.class, animation.getFrames()));
    currentAnimationActor.tell(new ShowFrame(), ActorRef.noSender());
  }
}
