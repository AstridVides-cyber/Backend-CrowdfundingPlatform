package com.example.crowdfundingplatform.export;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class WebExporter implements ExportStrategy {

    @Override
    public byte[] export(List<Campaign> campaigns) {
        StringBuilder sb = new StringBuilder();
        sb.append("<campaigns>\n");
        for (Campaign c : campaigns) {
            sb.append("  <campaign>\n")
                    .append("    <id>").append(c.getId()).append("</id>\n")
                    .append("    <title>").append(c.getTitle()).append("</title>\n")
                    .append("    <goal>").append(c.getGoal()).append("</goal>\n")
                    .append("    <status>").append(c.getStatus()).append("</status>\n")
                    .append("  </campaign>\n");
        }
        sb.append("</campaigns>");
        return sb.toString().getBytes(); // Convertido a bytes
    }

    @Override
    public String getFormat() {
        return "WEB";
    }
}