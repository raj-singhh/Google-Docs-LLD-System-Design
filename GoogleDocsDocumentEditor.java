import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Abstraction for document elements
abstract class DocumentElement {
    public abstract String render();
}

// Concrete implementation for text elements
class TextElement extends DocumentElement {
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public String render() {
        return text;
    }
}

// Concrete implementation for image elements
class ImageElement extends DocumentElement {
    private String imagePath;

    public ImageElement(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String render() {
        return "[Image: " + imagePath + "]";
    }
}

// NewLineElement represents a line break in the document.
class NewLineElement extends DocumentElement {
    @Override
    public String render() {
        return "\n";
    }
}

// TabSpaceElement represents a tab space in the document.
class TabSpaceElement extends DocumentElement {
    @Override
    public String render() {
        return "\t";
    }
}

// Document class responsible for holding a collection of elements
class Document {
    private List<DocumentElement> documentElements = new ArrayList<>();

    public void addElement(DocumentElement element) {
        documentElements.add(element);
    }

    // Renders the document by concatenating render outputs of all elements
    public String render() {
        StringBuilder result = new StringBuilder();
        for (DocumentElement element : documentElements) {
            result.append(element.render());
        }
        return result.toString();
    }
}

// Persistence abstraction
interface Persistence {
    void save(String data);
}

// FileStorage implementation of Persistence
class FileStorage implements Persistence {
    @Override
    public void save(String data) {
        try (FileWriter writer = new FileWriter("document.txt")) {
            writer.write(data);
            System.out.println("Document saved to document.txt");
        } catch (IOException e) {
            System.out.println("Error: Unable to save file.");
        }
    }
}

// Placeholder DBStorage implementation
class DBStorage implements Persistence {
    @Override
    public void save(String data) {
        // Save to DB (not implemented)
    }
}

// DocumentEditor class managing client interactions
class DocumentEditor {
    private Document document;
    private Persistence storage;
    private String renderedDocument;

    public DocumentEditor(Document document, Persistence storage) {
        this.document = document;
        this.storage = storage;
    }

    public void addText(String text) {
        document.addElement(new TextElement(text));
    }

    public void addImage(String imagePath) {
        document.addElement(new ImageElement(imagePath));
    }

    public void addNewLine() {
        document.addElement(new NewLineElement());
    }

    public void addTabSpace() {
        document.addElement(new TabSpaceElement());
    }

    public String renderDocument() {
        if (renderedDocument == null || renderedDocument.isEmpty()) {
            renderedDocument = document.render();
        }
        return renderedDocument;
    }

    public void saveDocument() {
        storage.save(renderDocument());
    }
}

// Client usage example (main method)
public class Main {
    public static void main(String[] args) {
        Document document = new Document();
        Persistence storage = new FileStorage();

        DocumentEditor editor = new DocumentEditor(document, storage);

        // Simulate client usage
        editor.addText("Hello, world!");
        editor.addNewLine();
        editor.addText("This is a real-world document editor example.");
        editor.addNewLine();
        editor.addTabSpace();
        editor.addText("Indented text after a tab space.");
        editor.addNewLine();
        editor.addImage("picture.jpg");

        // Render and print document
        System.out.println(editor.renderDocument());

        // Save to file
        editor.saveDocument();
    }
}
