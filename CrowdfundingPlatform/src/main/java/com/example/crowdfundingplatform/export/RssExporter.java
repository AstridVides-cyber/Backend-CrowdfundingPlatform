package com.example.crowdfundingplatform.export;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RssExporter implements ExportStrategy {

    @Override
    public String export(List<Campaign> campaigns) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<rss version=\"2.0\">\n<channel>\n");
        sb.append("  <title>Crowdfunding Platform</title>\n");
        for (Campaign c : campaigns) {
            sb.append("  <item>\n")
                    .append("    <title>").append(c.getTitle()).append("</title>\n")
                    .append("    <description>").append(c.getDescription()).append("</description>\n")
                    .append("    <goal>").append(c.getGoal()).append("</goal>\n")
                    .append("  </item>\n");
        }
        sb.append("</channel>\n</rss>");
        return sb.toString();
    }

    @Override
    public String getFormat() {
        return "RSS";
    }
}