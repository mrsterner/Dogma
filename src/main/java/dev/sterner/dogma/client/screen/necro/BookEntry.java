package dev.sterner.dogma.client.screen.necro;

import dev.sterner.dogma.client.screen.necro.page.BookPage;

import java.util.ArrayList;
import java.util.List;

public class BookEntry {
    public List<BookPage> pages = new ArrayList<>();

    protected BookEntry() {
    }

    public static BookEntry of() {
        return new BookEntry();
    }

    public BookEntry addPage(BookPage page) {
        if (page.isValid()) {
            pages.add(page);
        }
        return this;
    }
}