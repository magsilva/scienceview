/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis.preprocessing;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import topicevolutionvis.data.Corpus;
import topicevolutionvis.graph.Vertex;
import topicevolutionvis.matrix.SparseMatrix;
import topicevolutionvis.matrix.SparseVector;
import topicevolutionvis.preprocessing.singular.PlingStemmer;
import topicevolutionvis.preprocessing.steemer.Stemmer;
import topicevolutionvis.preprocessing.steemer.StemmerFactory;
import topicevolutionvis.preprocessing.steemer.StemmerType;
import topicevolutionvis.preprocessing.transformation.MatrixTransformationType;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.projection.temporal.TemporalProjection;

/**
 *
 * @author USER
 */
public abstract class Representation {

    protected Corpus corpus;
    protected StemmerType.Type stemmerType;
    protected MatrixTransformationType mattype;
    protected int numberGrams;
    protected int lowerCut, upperCut;
    protected boolean useStopword, resolve_plural = false, include_references = false;
    protected ArrayList<Ngram> ngrams;
    protected SparseMatrix sm;
    protected ProjectionData pdata;

    public Representation(Corpus corpus) {
        this.corpus = corpus;
    }

    public SparseMatrix getMatrix(int lowerCut, int upperCut, int numberGrams, StemmerType.Type stemmerType, MatrixTransformationType mattype, boolean useStopword, boolean include_references, boolean resolve_plural) throws IOException {
        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmerType = stemmerType;
        this.mattype = mattype;
        this.include_references = include_references;
        this.useStopword = useStopword;
        this.resolve_plural = resolve_plural;

        //store the ngrams present on the corpus
        this.ngrams = getCorpusNgrams();
        return this.getMatrix(corpus.getDocumentsIds(), null);
    }

    protected ArrayList<Ngram> getCorpusNgrams() throws IOException {
        HashMap<String, Double> corpusNgrams_aux = new HashMap<>();
        Stopwords stp = null;

        if (useStopword) {
            stp = Stopwords.getInstance();
        }
        String token;

        if (this.stemmerType == StemmerType.Type.NONE && this.resolve_plural) {
            for (Ngram n : this.corpus.getCorpusNgrams()) {
                token = n.ngram;
                if (useStopword && !stp.isStopWord(token)) {
                    token = StemmerFactory.getInstance(stemmerType).stem(token);
                    if (token.trim().length() > 0) {
                        token = PlingStemmer.stem(token);
                        if (corpusNgrams_aux.containsKey(token)) {
                            corpusNgrams_aux.put(token, corpusNgrams_aux.get(token) + n.frequency);
                        } else {
                            corpusNgrams_aux.put(token, n.frequency);
                        }
                    }
                }
            }
        } else {
            for (Ngram n : this.corpus.getCorpusNgrams()) {
                token = n.ngram;
                if (useStopword && !stp.isStopWord(token)) {
                    token = StemmerFactory.getInstance(stemmerType).stem(token);
                    if (token.trim().length() > 0) {
                        if (corpusNgrams_aux.containsKey(token)) {
                            corpusNgrams_aux.put(token, corpusNgrams_aux.get(token) + n.frequency);
                        } else {
                            corpusNgrams_aux.put(token, n.frequency);
                        }
                    }
                }
            }
        }

        double freq;
        ArrayList<Ngram> ngrams_aux = new ArrayList<>();
        for (Entry<String, Double> entry : corpusNgrams_aux.entrySet()) {
            freq = entry.getValue();
            if (upperCut >= 0) {
                if (freq >= lowerCut && freq <= upperCut) {
                    ngrams_aux.add(new Ngram(entry.getKey(), freq));
                }
            } else {
                if (freq >= lowerCut) {
                    ngrams_aux.add(new Ngram(entry.getKey(), freq));
                }
            }
        }

        Collections.sort(ngrams_aux);
        return ngrams_aux;
    }

    public SparseMatrix getMatrix(ProjectionData pdata) throws IOException {
        this.lowerCut = pdata.getLunhLowerCut();
        this.upperCut = pdata.getLunhUpperCut();
        this.numberGrams = corpus.getNumberGrams();
        this.stemmerType = pdata.getStemmer();
        this.useStopword = pdata.isUseStopword();
        this.include_references = pdata.getIncludeReferencesInBOW();

        //store the ngrams present on the corpus
        this.ngrams = this.getCorpusNgrams();
        return this.getMatrix(corpus.getDocumentsIds(), pdata);
    }

    public SparseMatrix getMatrixSelected(int lowerCut, int upperCut, int numberGrams,
            StemmerType.Type stemmer, boolean useStopword, boolean resolve_plural, ArrayList<Vertex> selected, TemporalProjection tprojection) throws IOException {

        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmerType = stemmer;
        this.useStopword = useStopword;
        this.resolve_plural = resolve_plural;

        int[] ids = new int[selected.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = selected.get(i).getId();
        }

        try{
        	//store the ngrams present on the selected corpus
            this.ngrams = this.getCorpusNgrams(ids);
        } catch (ClassCastException e) {
        	System.out.println("Erro getMatrix" + e.getMessage());
        }
       
        return this.getMatrix(ids, null);
    }

    protected ArrayList<Ngram> getCorpusNgrams(int[] urls) throws IOException {
        HashMap<String, Double> corpusNgrams_aux = new HashMap<>();
        Iterator<Map.Entry<String, Double>> iterator;
        Map.Entry<String, Double> entry;
        HashMap<String, Double> docNgrams;
        String token, new_token;

        Stopwords stp = null;

        if (useStopword) {
            stp = Stopwords.getInstance();
        }

        if (this.stemmerType == StemmerType.Type.NONE && this.resolve_plural) {
            for (int url : urls) {
                docNgrams = this.getNgrams(url);
                iterator = docNgrams.entrySet().iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    token = entry.getKey();
                    if (useStopword && !stp.isStopWord(token)) {
                        if (token.trim().length() > 0) {
                            new_token = PlingStemmer.stem(token);
                            if (corpusNgrams_aux.containsKey(new_token)) {
                                corpusNgrams_aux.put(new_token, corpusNgrams_aux.get(new_token) + docNgrams.get(token));
                            } else {
                                corpusNgrams_aux.put(new_token, docNgrams.get(token));
                            }
                        }
                    }
                }
            }
        } else {
            for (int url : urls) {
                docNgrams = this.getNgrams(url);
                iterator = docNgrams.entrySet().iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    token = entry.getKey();
                    if (useStopword && !stp.isStopWord(token)) {
                        if (corpusNgrams_aux.containsKey(token)) {
                            corpusNgrams_aux.put(token, corpusNgrams_aux.get(token) + docNgrams.get(token));
                        } else {
                            corpusNgrams_aux.put(token, docNgrams.get(token));
                        }
                    }

                }
            }
        }
        ArrayList<Ngram> ngrams_aux = new ArrayList<>();
        iterator = corpusNgrams_aux.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (upperCut >= 0) {
                if (entry.getValue() >= lowerCut && entry.getValue() <= upperCut) {
                    ngrams_aux.add(new Ngram(entry.getKey(), entry.getValue()));
                }
            } else {
                if (entry.getValue() >= lowerCut) {
                    ngrams_aux.add(new Ngram(entry.getKey(), entry.getValue()));
                }
            }
        }

        Collections.sort(ngrams_aux);
        return ngrams_aux;
    }

    protected HashMap<String, Double> getNgrams(Integer id) {
        try {
            HashMap<String, Double> ngrams_aux = new HashMap<>();
            Stopwords stp = null;
            if (useStopword) {
                stp = Stopwords.getInstance();
            }
            ArrayList<Ngram> fngrams = this.corpus.getNgrams(id);
            String token;
            Stemmer stemmer = StemmerFactory.getInstance(stemmerType);
            if (fngrams != null) {
                for (Ngram n : fngrams) {
                    token = n.ngram;
                    if (useStopword && !stp.isStopWord(token)) {
                        token = stemmer.stem(token);
                        if (token.trim().length() > 0) {
                                if (ngrams_aux.containsKey(token)) {
                                    ngrams_aux.put(token, ngrams_aux.get(token) + n.frequency);
                                } else {
                                    ngrams_aux.put(token, n.frequency);
                                }
                            }
                        }
                }
            }
            return ngrams_aux;
        } catch (IOException ex) {
            Logger.getLogger(VectorSpaceRepresentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Ngram> getNgramsAccordingTo(int lowerCut, int upperCut, int numberGrams,
            StemmerType.Type stemmer, boolean useStopword) throws IOException {

        this.lowerCut = lowerCut;
        this.upperCut = upperCut;
        this.numberGrams = numberGrams;
        this.stemmerType = stemmer;
        this.useStopword = useStopword;

        return this.getCorpusNgrams();
    }

    public ArrayList<Ngram> getNgrams() {
        return ngrams;
    }

    public abstract SparseMatrix getMatrix(int[] ids, ProjectionData pdata) throws IOException;
}
