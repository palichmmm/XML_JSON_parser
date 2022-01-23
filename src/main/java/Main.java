import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {
    public static String fileXML = "data.xml";
    public static String fileJSON = "data.json";
    public static void main(String[] args) throws Exception {
        List<Employee> list = parseXML(fileXML);
        String json = listToJson(list);
        if (writeString(json, fileJSON)) {
            System.out.println("Файл " + fileJSON + " успешно записан.");
        } else {
            System.out.println("Ошибка записи файла " + fileJSON + " !!!");
        }

    }
    public static List<Employee> parseXML(String file) throws Exception {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(file));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;
                list.add(new Employee(
                        Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent()),
                        employee.getElementsByTagName("firstName").item(0).getTextContent(),
                        employee.getElementsByTagName("lastName").item(0).getTextContent(),
                        employee.getElementsByTagName("country").item(0).getTextContent(),
                        Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent())
                ));
            }
        }
        return list;
    }
    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType(); // ???
        return gson.toJson(list, listType);
    }
    public static boolean writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
            return true;
        } catch (IOException err) {
            err.printStackTrace();
        }
        return false;
    }
}
