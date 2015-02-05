package gov.nih.nlm.nls.metamap.lite.context;

import java.util.List;
import java.util.ArrayList;
import gov.nih.nlm.nls.metamap.lite.types.Entity;
import gov.nih.nlm.nls.metamap.lite.types.BioCEntity;
import gov.nih.nlm.nls.types.Sentence;

import context.implementation.ConText;

import bioc.BioCSentence;
import bioc.BioCAnnotation;

/**
 * A wrapper around Wendy Chapman's ConTexT algorithm.
 * <p>
 * All methods run the context instance method: applyContext and the
 * return the result.
 */

public class ContextWrapper {
  static ConText contextInstance = new ConText();

  /** Given annotated sentence list with entities, determine hedging
   * relations using ConText.*/
  public static List<List<String>> applyContext(List<String> conceptList, List<String> sentenceList) 
    throws Exception {
    List<List<String>> resultlist = new ArrayList<List<String>>();
    for (String concept: conceptList) {
      for (String sentence: sentenceList) {
	resultlist.add(contextInstance.applyContext(concept, sentence));
      }
    }
    return resultlist;
  }

  /** Given annotated sentence list with entities, determine hedging
   * relations using ConText.*/
  public static List<List<String>> applyContextUsingEntities(List<Entity> entityList, 
							     List<Sentence> sentenceList) 
    throws Exception {
    List<List<String>> resultlist = new ArrayList<List<String>>();
    for (Entity entity: entityList) {
        for (Sentence sentence: sentenceList) {
            resultlist.add(contextInstance.applyContext(entity.getText(), sentence.getText()));
        }
    }
    return resultlist;
  }

  /** Given a set of annotated sentence with associated entities,
   * determine hedging relations using ConText.*/
  public static BioCSentence applyContext(BioCSentence sentence) throws Exception {
    for (BioCAnnotation annotation: sentence.getAnnotations()) {
      if (annotation instanceof BioCEntity) {
	for (Entity entity: ((BioCEntity)annotation).getEntitySet()) {
	  List<String> result = 
	    contextInstance.applyContext(entity.getMatchedText(), 
					 sentence.getText());
	  if (result != null) {
	    /** add negation information to existing annotation */
	    annotation.putInfon("concept",     result.get(0));
	    annotation.putInfon("sentence",    result.get(1));
	    annotation.putInfon("negstatus",   result.get(2));
	    annotation.putInfon("temporality", result.get(3));
	    annotation.putInfon("subject",     result.get(4));
	  }
	}
      }
    }
    return sentence;
  }

}
 
