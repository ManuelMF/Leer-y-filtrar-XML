package a03FicherosXML;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class a25Ejercicio22AñadirLineasEnXMLFiltrarXML {
	static DocumentBuilderFactory dbf;
	static DocumentBuilder db;
	static Document doc;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		boolean salir = false;
		String res, nombre, telefono;
		dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		doc = db.newDocument();

		String myHome = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		String archive = "AgendaTelefonicaXML.xml";
		Path PathArc = Paths.get(myHome + separator + archive);
		if (Files.notExists(PathArc)) {
			crearArchivo();
		}
		do {
			System.out.println("¿Que quieres hacer?");
			System.out.println("1) Añadir usuario");
			System.out.println("2) Buscar un Numero de telefono");
			System.out.println("3) Buscar nombre de usuario");
			res = sc.nextLine();
			if (res.equals("1")) {

				System.out.print("Nombre del usuario: ");
				nombre = sc.nextLine();
				System.out.print("Telefono del usuario: ");
				telefono = sc.nextLine();
				anyadirElemento(nombre, telefono);
			} else if (res.equals("2")) {

				System.out.print("Nombre de usuario: ");
				busqueda(sc.nextLine(), 0);

			} else if (res.equals("3")) {

				System.out.print("Número de teléfono: ");
				busqueda(sc.nextLine(), 1);

			}

		} while (salir == false);

	}

	public static void crearArchivo() {
		Element eRaiz;
		TransformerFactory transformerFactory;
		Transformer transformer = null;
		DOMSource source;
		StreamResult result;

		eRaiz = doc.createElement("Agenda");
		doc.appendChild(eRaiz);

		transformerFactory = TransformerFactory.newInstance();

		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}

		source = new DOMSource(doc);

		result = new StreamResult(new File("AgendaTelefonicaXML.xml"));

		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {

			e.printStackTrace();
		}

	}

	public static void anyadirElemento(String nombre, String telefono) {
		Node agenda = null, nNombre, nTelefono, nUsuario;
		File archivo;
		TransformerFactory transformerFactory;
		Transformer transformer;
		DOMSource source;
		StreamResult result;

		archivo = new File("AgendaTelefonicaXML.xml");

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();

			doc = db.parse(archivo);

			doc.getDocumentElement().normalize();

			agenda = doc.getFirstChild();
			
			nUsuario = doc.createElement("Usuario");
			agenda.appendChild(nUsuario);
			nNombre = doc.createElement("Nombre");
			nNombre.appendChild(doc.createTextNode(nombre));
			nUsuario.appendChild(nNombre);
			nTelefono = doc.createElement("Telefono");
			nTelefono.appendChild(doc.createTextNode(telefono));
			nUsuario.appendChild(nTelefono);

			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();

			source = new DOMSource(doc);

			result = new StreamResult(new File("AgendaTelefonicaXML.xml"));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void busqueda(String dat, int tipoBusqueda) {
		File archivo;
		String expresion;
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		Document documento = null;
		XPath ObjetoXPath;
		NodeList nUsuarios = null;
		
		archivo = new File("AgendaTelefonicaXML.xml");
		
		if (tipoBusqueda == 0) expresion = "//Agenda/Usuario[Nombre='" + dat + "']/Telefono";
		else expresion = "//Agenda/Usuario[Telefono='" + dat + "']/Nombre";
		
		dbFactory = DocumentBuilderFactory.newInstance();

		dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

		try {
			documento = dBuilder.parse(archivo);
		} catch (SAXException | IOException e) {

			e.printStackTrace();
		}

		ObjetoXPath = XPathFactory.newInstance().newXPath();

		try {
			nUsuarios = (NodeList) ObjetoXPath.evaluate(expresion, documento, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		System.out.println("Usuario:");
		for (int i = 0; i < nUsuarios.getLength(); i++) {
			if (tipoBusqueda==0) {
				System.out.println("Nombre: " + dat);
				System.out.println("Número: " + nUsuarios.item(i).getTextContent());
			} else {
				
				System.out.println("Nombre: " + nUsuarios.item(i).getTextContent());
				System.out.println("Número: " + dat);
			}
			
		}
	}
}
