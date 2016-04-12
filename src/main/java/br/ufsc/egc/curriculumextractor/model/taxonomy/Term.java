package br.ufsc.egc.curriculumextractor.model.taxonomy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Term implements Serializable {
	
	private static final Logger LOGGER = Logger.getLogger(Term.class);

	private String label;
	private List<Term> sons;
	
	private Term parent;
	
	public Term() {
		label = "";
		sons = new ArrayList<Term>();
		parent = null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Term> getSons() {
		return sons;
	}

	public void setSons(List<Term> sons) {
		this.sons = sons;
	}
	
	public Term getParent() {
		return parent;
	}
	
	private void setParent(Term parent) {
		this.parent = parent;
	}
	
	public void addSons(List<Term> terms) {
		for (Term term: terms) {
			addSon(term);
		}
	}
	
	public void addSon(Term term) {
		if (this == term) {
			throw new RuntimeException("Quebra de árvore");
		}
		if (sons.contains(term)) {
			LOGGER.warn("Tentativa de inserir elemento já existente. Ignorando...");
			return;
		}
		term.setParent(this);
		sons.add(term);
	}
	
	public Term find(String label, boolean ignoreCase) {
		return find(this, label, ignoreCase);
	}

	private Term find(Term term, String label, boolean ignoreCase) {
		if ((ignoreCase && label.equalsIgnoreCase(term.getLabel())) || (label.equals(term.getLabel()))) {
			return term;
		} else {
			Term retorno = null;
			for (Term son: term.getSons()) {
				retorno = find(son, label, ignoreCase);
				if (retorno != null) {
					return retorno;
				}
			}
			return retorno;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((sons == null) ? 0 : sons.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (sons == null) {
			if (other.sons != null)
				return false;
		} else if (!sons.equals(other.sons))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Term [label=" + label + ", sons=" + sons + ", parent=" + parent
				+ "]";
	}

}
