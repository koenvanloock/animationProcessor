package com.example.demo.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.example.demo.models.ShowFrame;
import com.example.demo.models.StartAnimation;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class AnimationActor extends AbstractActor {

  private LinkedList<String> frames;

  public AnimationActor(ArrayList<String> frames) {
    this.frames = new LinkedList<>();
    this.frames.addAll(frames);
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create()
        .match(StartAnimation.class, startAnimation -> showFrameAndScheduleNext())
        .match(ShowFrame.class, showFrame -> showFrameAndScheduleNext())
        .build();
  }

  private void showFrameAndScheduleNext() {
    String anim = frames.pop();
    System.out.println(anim);
    frames.addLast(anim);

    getContext().getSystem().scheduler()
        .scheduleOnce(
            FiniteDuration.apply(40, TimeUnit.MILLISECONDS),
            getSelf(), new ShowFrame(),
            getContext().getSystem().dispatcher(),
            null);
  }
}
