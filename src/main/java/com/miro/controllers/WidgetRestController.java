package com.miro.controllers;

import com.miro.model.Widget;
import com.miro.model.WidgetDto;
import com.miro.services.WidgetService;
import com.miro.utils.WidgetMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/widgets")
public class WidgetRestController {

    private static final int MAX_PAGE_SIZE = 500;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int ZERO_PAGE_NUM = 0;

    final
    WidgetService widgetService;

    public WidgetRestController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public ResponseEntity<Widget> createWidget(@RequestBody WidgetDto widgetDto) {
        Widget widget = WidgetMapper.convertToEntity(widgetDto);
        Widget storedWidget = widgetService.save(widget);
        return new ResponseEntity<>(storedWidget, HttpStatus.CREATED);
    }

    @PutMapping("/{widgetId}")
    public ResponseEntity<Widget> updateWidget(@RequestBody WidgetDto widget, @PathVariable Long widgetId) {
        Optional<Widget> optionalWidget = widgetService.get(widgetId);
        if (optionalWidget.isPresent()) {
            Widget oldWidget = optionalWidget.get();
            oldWidget.setZIndex(widget.getZIndex());
            oldWidget.setHeight(widget.getHeight());
            oldWidget.setWidth(widget.getWidth());
            oldWidget.setHeight(widget.getHeight());
            oldWidget.setLastModificationTs(Instant.now());
            return new ResponseEntity<>(widgetService.save(oldWidget), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{widgetId}")
    public ResponseEntity<String> deleteWidget(@PathVariable Integer widgetId) {
        widgetService.delete(widgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{widgetId}")
    public ResponseEntity<Widget> getWidgetById(@PathVariable Long widgetId) {
        Optional<Widget> optionalWidget = widgetService.get(widgetId);
        if (optionalWidget.isPresent()) {
            Widget widget = optionalWidget.get();
            return new ResponseEntity<>(widget, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Widget>> getWidgets(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (page == null) {
            page = ZERO_PAGE_NUM;
        }
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        Page<Widget> widgets = widgetService.getAll(page, size);
        return new ResponseEntity<Page<Widget>>(widgets, HttpStatus.OK);
    }
}