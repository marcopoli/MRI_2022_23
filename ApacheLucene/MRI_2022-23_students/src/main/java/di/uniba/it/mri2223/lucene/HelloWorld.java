package di.uniba.it.mri2223.lucene;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
*
* @author marco
*/
public class HelloWorld {

   /**
    * @param args the command line arguments
    */
	public static void main(String[] args) {
       try {
           //Open a directory from the file system (index directory)
           FSDirectory fsdir = FSDirectory.open(new File("./resources/helloworld").toPath());
           //IndexWriter configuration
           IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
           //Index directory is created if not exists or overwritten
           iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
           //Create IndexWriter
           IndexWriter writer = new IndexWriter(fsdir, iwc);
           //Create document and add fields
           Document doc = new Document();
           doc.add(new TextField("super_name", "Spider-Man", Field.Store.YES));
           doc.add(new TextField("name", "Peter Parker", Field.Store.YES));
           doc.add(new TextField("category", "superhero", Field.Store.NO));
           doc.add(new TextField("powers", "agility, spider-sense", Field.Store.NO));
           //add document to index
           writer.addDocument(doc);
           //close IndexWriter
           writer.close();
           //Create the IndexSearcher
           IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));
           //Create the query parser with the default field and analyzer
           QueryParser qp = new QueryParser("name", new StandardAnalyzer());
           //Parse the query
           Query q = qp.parse("peter powers:agility");
           //Search
           TopDocs topdocs = searcher.search(q, 10);
           System.out.println("Found " + topdocs.totalHits.value + " document(s).");
           
       } catch (IOException | ParseException ex) {
           Logger.getLogger(HelloWorld.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

}