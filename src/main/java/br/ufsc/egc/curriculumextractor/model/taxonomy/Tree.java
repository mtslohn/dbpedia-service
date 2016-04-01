package br.ufsc.egc.curriculumextractor.model.taxonomy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class Tree {

	private static final Logger LOGGER = Logger.getLogger(Tree.class);

	private List<Term> roots;

	public Tree() {
		roots = new ArrayList<Term>();
	}

	public List<Term> getRoots() {
		return roots;
	}

	public void setRoots(List<Term> roots) {
		this.roots = roots;
	}

	public Term find(String label) {
		return find(roots, label);
	}

	private Term find(List<Term> terms, String label) {
		for (Term term : terms) {
			if (term.getLabel().equals(label)) {
				return term;
			} else {
				Term result = find(term.getSons(), label);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	public void addRoot(Term term) {
		roots.add(term);
	}

	public String print() {
		StringBuilder builder = new StringBuilder();
		print(builder, "", roots);
		return builder.toString();
	}

	private void print(StringBuilder builder, String prefix, List<Term> terms) {
		for (int index = 0; index < terms.size(); index++) {
			String innerPrefix = prefix;
			if (!prefix.isEmpty()) {
				innerPrefix = prefix + "-";
			}
			innerPrefix = innerPrefix + (index + 1);
			Term term = terms.get(index);
			builder.append(innerPrefix + ": " + term.getLabel() + "\n");
			print(builder, innerPrefix, term.getSons());
		}
	}

	public Tree clean(Collection<String> entityList) {
		Tree tree = new Tree();
		for (Term root : getRoots()) {
			createCleanTree(tree, null, root, entityList);
		}
		return tree;
	}

	private void createCleanTree(Tree tree, Term parent, Term current,
			Collection<String> entityList) {
		for (Term son : current.getSons()) {
			if (hasTerm(entityList, son.getLabel())) {
				String newParentLabel = null;
				if (parent != null) {
					newParentLabel = parent.getLabel();
				}
				tree.addToTree(newParentLabel, son.getLabel());
				createCleanTree(tree, son, son, entityList);
			}
			createCleanTree(tree, parent, son, entityList);
		}
	}

	private boolean hasTerm(Collection<String> entityList, String label) {
		for (String entity: entityList) {
			if (entity.equalsIgnoreCase(label)) {
				return true;
			}
		}
		return false;
	}

	public void addToTree(String broader, String narrower) {
		Term sonTerm = new Term();
		sonTerm.setLabel(narrower);

		if (broader == null) {
			addRoot(sonTerm);
		} else {
			if (broader.equalsIgnoreCase(narrower)) {
				LOGGER.warn("Tentativa de inserir pai e filhos iguais. Abortando...");
				return;
			}
			Term term = find(broader);
			if (term == null) {
				term = new Term();
				term.setLabel(broader);
				addRoot(term);
			}
			term.addSon(sonTerm);
		}

	}

}
