package com.ykgroup.dayco.posts.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ykgroup.dayco.posts.domain.Posts;
import com.ykgroup.dayco.posts.infra.PostsJpaRepository;
import com.ykgroup.dayco.posts.ui.dto.PostsListResponseDto;
import com.ykgroup.dayco.posts.ui.dto.PostsResponseDto;
import com.ykgroup.dayco.posts.ui.dto.PostsSaveRequestDto;
import com.ykgroup.dayco.posts.ui.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsJpaRepository postsJpaRepository;

    @Transactional
    public Posts save(PostsSaveRequestDto requestDto) {
        return postsJpaRepository.save(
                new Posts(requestDto.getTitle(),
                          requestDto.getContent(),
                          requestDto.getAuthor()));
    }

    @Transactional
    public Posts update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsJpaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("There are not posts. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return posts;
    }

    public PostsResponseDto find(Long id) {
        Posts entity = postsJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There are not posts. id = " + id));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There are not posts. id = " + id));
        postsJpaRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAll() {
        return postsJpaRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
