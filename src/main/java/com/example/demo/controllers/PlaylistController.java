package com.example.demo.controllers;

import akka.actor.ActorRef;
import com.example.demo.models.Animation;
import com.example.demo.models.StartPlaylist;
import com.example.demo.models.StopPlaylist;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/playlist")
public class PlaylistController {

  private ActorRef playlistActor;

  public PlaylistController(@Qualifier("playlistActor") ActorRef playlistActor) {
    this.playlistActor = playlistActor;
  }

  @PostMapping("/start")
  public ResponseEntity<?> startPlaylist(@RequestBody List<Animation> animationList) {
    playlistActor.tell(new StartPlaylist(animationList), ActorRef.noSender());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/end")
  public ResponseEntity<?> startPlaylist() {
    playlistActor.tell(new StopPlaylist(), ActorRef.noSender());
    return ResponseEntity.ok().build();
  }


}
