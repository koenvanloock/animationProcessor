package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StartPlaylist {
  private List<Animation> animations;
}
