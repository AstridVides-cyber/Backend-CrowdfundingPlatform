package com.example.crowdfundingplatform.export;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CsvExporter implements ExportStrategy {

    @Override
    public String export(List<Campaign> campaigns) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,title,goal,status,category,location\n");
        for (Campaign c : campaigns) {
            sb.append(c.getId()).append(",")
                    .append(c.getTitle()).append(",")
                    .append(c.getGoal()).append(",")
                    .append(c.getStatus()).append(",")
                    .append(c.getCategory()).append(",")
                    .append(c.getLocation()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getFormat() {
        return "CSV";
    }
}