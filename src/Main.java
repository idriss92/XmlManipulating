import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class Main {
	
	public static void main (String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		System.out.println("Cette application vous permet d'effectuer 3 oprations ");
		
		System.out.println("Menu");
		System.out.println("Pour crer une cole taper 0");
		System.out.println("Pour valider un fichier xml  partir du schma xml taper 1");
		System.out.println("Pour valider un fichier xml  partir de la dtd taper 2");
		
		System.out.println();
		System.out.println("Entrer votre choix : ");
		Scanner sc = new Scanner(System.in);
		int choix = sc.nextInt();
		
		switch(choix){
			
		case 0 :
			System.out.println("Choix de cration valide");
			System.out.println();
			createSchool();
			break;
		case 1 :
			System.out.println("Choix de validation avec .XSD");
			System.out.println();
			System.out.println("Entrer le nom du fichier Xml prcd du chemin : ");
			Scanner recup = new Scanner (System.in);
			String entree1 = recup.nextLine();
			System.out.println("Entrer le nom du fichier XSD prcd du chemin : ");
			Scanner recupe = new Scanner (System.in);
			String entree2 = recupe.nextLine();
			validateXMLSchema(entree1,entree2);
			break;
			
		case 2 : 
			System.out.println("Choix de validation avec .DTD");
			System.out.println();
			System.out.println("Entrer le nom du fichier Xml prcd du chemin : ");
			Scanner recu = new Scanner (System.in);
			String entre1 = recu.nextLine();
			System.out.println("Entrer le nom du fichier DTD prcd du chemin : ");
			Scanner recue = new Scanner (System.in);
			String entre2 = recue.nextLine();
			validateMyDtd(entre1,entre2);
			break;
			
		default :
			System.out.println("Votre choix n'est pas pris en compte. Dsole");
			
		}
		
		//validateXMLFileWithXsd("/Users/sani/Documents/xml/Ecole.xml","/Users/sani/Documents/xml/Ecole.xsd");
		
		
		
	}
	
	static private void createSchool(){
		
		
		try{
			DocumentBuilderFactory dbfe = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbfe.newDocumentBuilder();
			Document doc = db.newDocument();
			
			//Construction racine
			Element root = doc.createElement("Ecole");
			int promo;
			System.out.println("Entrer le nombre de promo");
			Scanner sc = new Scanner(System.in);
			promo = sc.nextInt();
			
			for(int i =0; i<promo;i++){
				Element eltPromo = doc.createElement("promo");
				System.out.println("Entrer le nom de la promo : ");
				Scanner s = new Scanner(System.in);
				String nomPromo = s.nextLine();
				eltPromo.setAttribute("nom", nomPromo);
				
				//boolean decistion = false;
				
				System.out.println("Voulez vous creer des leves ?");
				Scanner des = new Scanner(System.in);
				String reponse = des.nextLine();
				
				if(reponse =="O" ){
					return;
				}
				else{
					System.out.println("Combien d'tudiants voulez vous crer : ");
					Scanner te = new Scanner(System.in);
					int nombreEleve = te.nextInt();
					Element listEleve = doc.createElement("Eleves");
					
					for(int p = 0; p<nombreEleve; p++){
						Element eltEleve = doc.createElement("Eleve");
						System.out.println("Identifiant de l'eleve : ");
						
						Scanner dep = new Scanner(System.in);
						String id = dep.nextLine();
						eltEleve.setAttribute("identifiant", id);
						
						System.out.println("Nom de l'eleve : ");
						Scanner depa = new Scanner(System.in);
						String name = depa.nextLine();
						eltEleve.setTextContent(name);
						//eltEleve.setAttribute("nom", name);
						listEleve.appendChild(eltEleve);
					}
					eltPromo.appendChild(listEleve);
				}
				root.appendChild(eltPromo);
				
				
			}
			
			doc.appendChild(root);
			
			generateXMLFile(doc,new File("/Users/sani/Documents/Ecole.xml"));
			
		}
		catch(Exception e){
			System.err.println(e);
		}
		
	}
	
	static private void generateXMLFile(Document doc, File file){
        Source source = new DOMSource(doc);
        // le rsultat de cette transformation sera un flux d'criture dans
        // un fichier
        StreamResult resultat = new StreamResult(file);
         
        // cration du transformateur XML
        Transformer transfo = null;
        try {
            transfo = TransformerFactory.newInstance().newTransformer();
        } catch(TransformerConfigurationException e) {
            System.err.println("Impossible de crer un transformateur XML.");
            System.exit(1);
        }
         
        // configuration du transformateur
         
        // sortie en XML
        transfo.setOutputProperty(OutputKeys.METHOD, "xml");
         
        // inclut une dclaration XML (recommand)
        transfo.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
         
        // codage des caractres : UTF-8. Ce pourrait tre galement ISO-8859-1
        transfo.setOutputProperty(OutputKeys.ENCODING, "utf-8");
         
        // idente le fichier XML
        transfo.setOutputProperty(OutputKeys.INDENT, "yes");
         
        try {
            transfo.transform(source, (javax.xml.transform.Result) resultat);
        } catch(TransformerException e) {
            System.err.println("La transformation a chou : " + e);
            System.exit(1);
        }

}
	
	static private void validateXMLSchema(String xmlPath,String xsdPath){
		
		try{
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xmlPath)));
			System.out.println("Le fichier "+ xmlPath+ "est validŽ par rapport au fichier "+xsdPath);
		}
		catch(IOException|SAXException e){
			
			System.out.println("Le fichier est invalide car "+ e.getMessage());
		}
	}

	static private void validateMyDtd( String xmlPath, String dtdPath) {
        try {
            File xmlwithDtd = new File("XmlWithDTD.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdPath);

            transformer.transform(new StreamSource(xmlPath), new StreamResult(xmlwithDtd));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Set the error handler
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                @Override
                public void fatalError(SAXParseException spex) {
                    System.out.println("fatal error : " + spex.getMessage());
                }
                @Override
                public void error(SAXParseException spex) throws SAXException {
                    throw spex;
                }
                @Override
                public void warning(SAXParseException spex) {
                    System.out.println("warning : " + spex.getMessage());
                }
            });

            builder.parse(xmlwithDtd);
            xmlwithDtd.delete();
            System.out.println("Succes");
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Fail");
        	
        }
    }

}