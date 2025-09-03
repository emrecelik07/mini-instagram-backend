package com.emrecelik.mini_instagram_backend.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    private String caption;
    private String imageUrl;
    private String videoUrl;
    private Boolean isPrivate;
    private String location;
    private String tags;
}
