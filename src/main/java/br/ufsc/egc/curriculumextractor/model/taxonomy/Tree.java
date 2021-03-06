package br.ufsc.egc.curriculumextractor.model.taxonomy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class Tree implements Serializable {

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
		Collections.sort(terms);
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
			} else {
				createCleanTree(tree, parent, son, entityList);
			}
		}
	}

	private boolean hasTerm(Collection<String> entityList, String label) {
		for (String entity : entityList) {
			if (entity.equalsIgnoreCase(label)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasTerm(List<Term> terms, String label) {
		for (Term term : terms) {
			if (term.getLabel().equalsIgnoreCase(label)) {
				return true;
			}
		}
		return false;
	}

	public void addToTree(String broader, String narrower) {
		Term sonTerm = new Term();
		sonTerm.setLabel(narrower);

		if (broader == null) {
			if (hasTerm(getRoots(), narrower)) {
				LOGGER.warn("Tentativa de elemento já existente. Abortando...");
				return;
			}
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
			if (hasTerm(term.getSons(), narrower)) {
				LOGGER.warn("Tentativa de elemento já existente. Abortando...");
				return;
			}
			term.addSon(sonTerm);
		}

	}

	public void join() {

		List<Term> rootsClone = new ArrayList<Term>(getRoots());

		for (Term selectedRoot : rootsClone) {
			for (Term root : rootsClone) {
				if (root != selectedRoot) {
					moveRootIfPossible(selectedRoot, root);
				}
			}
		}
	}

	private boolean moveRootIfPossible(Term selectedRoot, Term term) {
		if (term.getLabel().equalsIgnoreCase(selectedRoot.getLabel())
				&& (isNotRoot(term))) {
			term.addSons(selectedRoot.getSons());
			getRoots().remove(selectedRoot);
			return true;
		} else {
			for (Term son : term.getSons()) {
				if (moveRootIfPossible(selectedRoot, son)) {
					return true;
				}
			}
			return false;
		}
	}

	private boolean isNotRoot(Term term) {
		return !getRoots().contains(term);
	}

}
