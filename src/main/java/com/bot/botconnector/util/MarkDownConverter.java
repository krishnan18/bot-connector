package com.bot.botconnector.util;

import org.springframework.stereotype.Component;

@Component
public class MarkDownConverter {
    private static final String OPTIONS_PATTERN = "(<li><a [^>]*(href=\"([^>^\"]*)\")*(data-vtz-jump=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a><\\/li>)";
    private static final String ANCHOR_LINK_PATTERN_1 = "<a[^>]*(data-vtz-browse=\"([^>^\"]*)\")[^>]*(data-vtz-link-type=\"([^>^\"]*)\")[^>]*(rel=\"([^>^\"]*)\")[^>]*>([^<]+)<\\/a>";
    private static final String ANCHOR_LINK_PATTERN_2 = "(<a [^>]*(href=\"([^>^\"]*)\")*(data-vtz-browse=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a>)";
    private static final String BUTTON_LINK_PATTERN_1 = "(<button><a [^>]*(href=\"([^>^\"]*)\")*(data-vtz-browse=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a><\\/button>)";
    private static final String BUTTON_LINK_PATTERN_2 = "(<button><a [^>]*(href=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a><\\/button>)";
    private static final String SECONDARY_BUTTON_OPTION_PATTERN = "(<blockquote (style=\"([^>^\"]*)\")><button><a [^>]*(href=\"#\")*(data-vtz-jump=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a><\\/button><\\/blockquote>)";
    private static final String SECONDARY_BUTTON_LINK_PATTERN = "(<blockquote (style=\"([^>^\"]*)\")><button><a [^>]*(href=\"([^>^\"]*)\")[^>]*>)([^<]+)(<\\/a><\\/button><\\/blockquote>)";
    private static final String ORDERED_LIST_PATTERN = "<ol><li>((?!<a).*?)<\\/li>";
    private static final String UNORDERED_LIST_PATTERN_1 = "<ul><li>((?!<a).*?)<\\/li>";
    private static final String UNORDERED_LIST_PATTERN_2 = "<ul><li>(<a\\s(href=\"tel:).*?)<\\/li>";
    private static final String UL_END_TAG_PATTERN = "</ul>";
    private static final String OL_END_TAG_PATTERN = "</ol>";
    private static final String TEL_TAG_PATTERN = "<li>(<a\\s(?!(href=\"tel:)).*?)<\\/li>";

    public static String convertHtmlToMarkDown(String html) {
        if(html == null)
            return null;
        html = html.replaceAll(OPTIONS_PATTERN,"\n[op] $6 [/op] [id = \"$5\"]");
        html = html.replaceAll(SECONDARY_BUTTON_OPTION_PATTERN,"\n[sop] $7 [/sop] [id = \"$6\"]");
        html = html.replaceAll(BUTTON_LINK_PATTERN_1,"\n[pbl] $6 [/pbl] [link = \"$5\"]");
        html = html.replaceAll(BUTTON_LINK_PATTERN_2,"\n[pbl] $4 [/pbl] [link = \"$3\"]");
        html = html.replaceAll(SECONDARY_BUTTON_LINK_PATTERN,"\n[sbl] $6 [/sbl] [link = \"$5\"]");
        html = html.replaceAll(ORDERED_LIST_PATTERN,"\n<ol>\n <li>$1</li>\n");
        html = html.replaceAll(UNORDERED_LIST_PATTERN_1,"\n<ul>\n <li>$1</li>\n");
        html = html.replaceAll(UNORDERED_LIST_PATTERN_2,"\n<ul>\n <li>$1</li>\n");
        html = html.replaceAll(ANCHOR_LINK_PATTERN_1,"\n[link] $7 [/link] [link = \"$2\"]");
        html = html.replaceAll(ANCHOR_LINK_PATTERN_2,"\n[link] $6 [/link] [link = \"$5\"]");
        html = html.replaceAll(UL_END_TAG_PATTERN,"\n</ul>");
        html = html.replaceAll(OL_END_TAG_PATTERN,"\n</ol>");
        html = html.replaceAll(TEL_TAG_PATTERN,"\n<li>$1</li>");
        html = html.replace("<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">","");
        html = html.replace("<div class=\"nw_options_end\"></div>","");
        html = html.replace("</div>","");
        return html;
    }
}
