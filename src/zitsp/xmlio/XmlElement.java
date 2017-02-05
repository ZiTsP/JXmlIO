package zitsp.xmlio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class XmlElement {

    private final String name;
    
    public XmlElement(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    private Optional<String> text = Optional.empty();
    
    public XmlElement setElement(String element) {
        this.text = Optional.ofNullable(element);
        return this;
    }
    
    public boolean hasText() {
        return this.text.isPresent();
    }
    
    public Optional<String> getText() {
        return this.text;
    }

    public boolean isIndependentTag() {
        return (!this.text.isPresent() && this.children.isEmpty());
    }
    
    public Optional<String> getIndependentTag() {
        if (this.isIndependentTag()) {
            StringJoiner str = new StringJoiner(" ", "<", " />");
            str.add(this.getName());
            this.getAttributesLine().ifPresent(attribute -> str.add(attribute));
            return Optional.of(str.toString());
        }
        return Optional.empty();
        
    }
    
    public String getStartTag() {
        StringJoiner str = new StringJoiner(" ", "<", ">");
        str.add(this.getName());
        this.getAttributesLine().ifPresent(attribute -> str.add(attribute));
        return str.toString();
    }
    
    public String getEndTag() {
        StringBuffer str = new StringBuffer("</");
        str.append(this.getName());
        str.append(">");
        return str.toString();
    }
    
    private List<XmlElement> children = new ArrayList<>();

    public XmlElement setChild(XmlElement child) {
        this.children.add(child);
        return this;
    }
    
    public XmlElement setChild(List<XmlElement> children) {
        this.children.addAll(children);
        return this;
    }
    
    public boolean hasChild() {
        return !this.children.isEmpty();
    }
    
    public List<XmlElement> getChildren() {
        return this.children;
    }
    
    private Map<String, String> attributes = new HashMap<>();
    
    public void setAttribute(String name, String text) {
        if (name != null && text != null) {
            attributes.put(name, text);
        }
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public boolean hasAttribute() {
        return !this.attributes.isEmpty();
    }
    
    public Optional<String> getAttributesLine() {
        if (!this.hasAttribute()) {
            return Optional.empty();
        }
        StringJoiner str = new StringJoiner(" ");
        this.attributes
            .forEach((k, v) -> str.add((new StringBuffer()).append(k).append("=\"").append(v).append("\"").toString()));
        return Optional.of(str.toString());
    }
    
}
