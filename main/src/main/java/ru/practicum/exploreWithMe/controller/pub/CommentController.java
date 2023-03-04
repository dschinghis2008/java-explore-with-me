package ru.practicum.exploreWithMe.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Validated
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{id}")
    public CommentOutDto getById(@PathVariable long id) {
        return commentMapper.toOutDto(commentService.getById(id));
    }

    @GetMapping
    public List<CommentOutDto> getAllByEvent(@RequestParam long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getAllByEventId(eventId, from, size).stream()
                .map(commentMapper::toOutDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/tst")
    public String tst(){
        String res = "<html><body><p id='p1'>xxx</p><button onclick='b1()'>GETDATA</button></body></html>";
        res += "<script>\n" +
                "function b1() {\n" +
                "    var reqUrl = 'http://localhost:8080/comments/1';\n" +
                "    var req = new XMLHttpRequest();\n" +
                "    req.open('GET', reqUrl);\n" +
                "    req.responseType = 'json';\n" +
                "    req.send();\n" +
                "    req.onload = function () {\n" +
                "        var comment = req.response;\n" +
                "        document.getElementById('p1').innerHTML = reqUrl + '<br/>text: ' + comment.text + \n" +
                "    '<br/>date: ' + comment.created + '<br/>visible: ' + comment.visible;" +
                "    }\n" +
                "}\n" +
                "</script>";
        return res;
    }

}
