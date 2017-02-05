package zitsp.xmlio;

import java.util.Optional;

public class XmlElementManager {
    
    private XmlElementManager() {
    }

    public static Optional<XmlElement> searchTag(String tagName, XmlElement root) {
        if (root.getName().equals(tagName)) {
            return Optional.of(root);
        } else {
            Optional<XmlElement> child = root.getChildren().stream().filter(e -> e.getName().equals(tagName)).findFirst();
            if (child.isPresent()) {
                return child;
            } else {
                Optional<XmlElement> deepMatch = root.getChildren().stream().map(e -> searchTag(tagName, e).orElse(null)).filter(e -> e != null).findAny();
                return deepMatch.isPresent() ? deepMatch : Optional.empty();
            }
        }
    }
    
    public static Optional<String> searchTagText(String tagName, XmlElement root) {
        if (root.getName().equals(tagName)) {
            return root.getText();
        } else {
            Optional<XmlElement> child = root.getChildren().stream().filter(e -> e.getName().equals(tagName)).findFirst();
            if (child.isPresent()) {
                return child.get().getText();
            } else {
                Optional<String> deepMatch = root.getChildren().stream().map(e -> searchTagText(tagName, e).orElse(null)).filter(e -> e != null).findAny();
                return deepMatch.isPresent() ? deepMatch : Optional.empty();
            }
        }
    }

}
