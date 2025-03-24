package EstructuraArbre;

import Alumnes.Alumnes_SEC;

import java.util.Queue;

public class AcbEnll <E extends Comparable<E>> implements Acb<E>{

    private class NodeA implements Cloneable{
        E info;
        NodeA dret;
        NodeA esq;

        public NodeA(NodeA dret, NodeA esq, E info) {
            this.info = info;
            this.dret = dret;
            this.esq = esq;
        }
        public NodeA(E inf){
            this(null, null, inf);
        }

        // ↓ Tira true si l'element ja existeix en l'arbre
        private boolean membreRecursive(E element){
            if (((Alumnes_SEC) element).compareTo((Alumnes_SEC) info) == 0
                && ((Alumnes_SEC) element).equals((Alumnes_SEC) info)){
                return true;
            }
            if (dret != null && ((Alumnes_SEC) element).compareTo((Alumnes_SEC) info) > 0){
                return dret.membreRecursive(element);
            }
            if (esq != null && ((Alumnes_SEC) element).compareTo((Alumnes_SEC) info) < 0){
                return esq.membreRecursive(element);
            }
            return false;

            /* NO ES EFICIENTE PARA ACB

            if (info.equals(element)){
                return true;
            }
            if (dret != null){
                return dret.membreRecursive(element);
            }
            if (esq != null){
                return esq.membreRecursive(element);
            }

            return false;
            */
        }

        // ↓ Posa un element a l'arbre però tira excepció si ja existeix
        private void inserirRecursive(E element) throws ArbreException{
            if (membre(element)){
                throw new ArbreException("Element ja existeix");
            }

            if (element.compareTo(info) > 0){
                if (dret == null){
                    dret = new NodeA(element);
                } else {
                    dret.inserirRecursive(element);
                }
            } else {
                if (esq == null){
                    esq = new NodeA(element);
                } else {
                    esq.inserirRecursive(element);
                }
            }
        }

        // ↓ Esborra l'element més petit de l'arbre
        private E esborrarMinim(NodeA node) throws ArbreException {
            if (info == null){
                throw new ArbreException("Arbre buit");
            }
            if (!membre(node.info)){
                throw new ArbreException("Element no existeix");
            }

            if (node.esq == null){
                E aux = node.info;
                node = node.dret;
                return aux;
            }
            return esborrarMinim(node.esq);

        }

        private NodeA esborrarRecursive(E element) throws ArbreException {
            if (((Alumnes_SEC) element).compareTo((Alumnes_SEC) info) < 0) {
                if (esq == null) {
                    throw new ArbreException("Element no trobat");
                }
                esq = esq.esborrarRecursive(element);
            } else if (element.compareTo(info) > 0) {
                if (dret == null) {
                    throw new ArbreException("Element no trobat");
                }
                dret = dret.esborrarRecursive(element);
            } else {
                if (esq == null) {
                    return dret;
                }
                if (dret == null) {
                    return esq;
                }
                info = dret.esborrarMinim(dret);
                dret = dret.esborrarRecursive(info);
            }
            return this;
        }

        // Omple la cua amb elements en ordre ascendent (Inordre)
        private void omplirInOrdre(Queue<E> cua) {
            if (esq != null) esq.omplirInOrdre(cua);
            cua.add(info);
            if (dret != null) dret.omplirInOrdre(cua);
        }

        // Omple la cua amb elements en ordre descendent (Inordre invers)
        private void omplirReverseInOrdre(Queue<E> cua) {
            if (dret != null) dret.omplirReverseInOrdre(cua);
            cua.add(info);
            if (esq != null) esq.omplirReverseInOrdre(cua);
        }

        // Retorna el nombre total de nodes (recursivo)
        private int cardinalitatRecursive() {
            int count = 1; // Contamos el nodo actual
            if (esq != null) count += esq.cardinalitatRecursive();
            if (dret != null) count += dret.cardinalitatRecursive();
            return count;
        }

        // Mètode clone per suportar subarbres
        @Override
        public Object clone() {
            NodeA nouNode = new NodeA(info);
            if (esq != null) nouNode.esq = (NodeA) esq.clone();
            if (dret != null) nouNode.dret = (NodeA) dret.clone();
            return nouNode;
        }

    }

    private NodeA arrel; // Nodo raíz del árbol

    public AcbEnll(NodeA arrel) {
        this.arrel = arrel; // Inicializa con un nodo raíz dado
    }

    public AcbEnll() {
        this(null); // Árbol vacío
    }

    @Override
    public E arrel() throws ArbreException {
        if (arrel == null) {
            throw new ArbreException("Arbre buit");
        }
        return arrel.info;
    }

    @Override
    public Acb<E> fillEsquerre() throws CloneNotSupportedException {
        if (arrel == null || arrel.esq == null) {
            return new AcbEnll<>(); // Árbol vacío
        }
        return new AcbEnll<>((NodeA) arrel.esq.clone());
    }
    // ↑ ↓ ambdós cast no hauríen de donar problemes
    @Override
    public Acb<E> fillDret() throws CloneNotSupportedException {
        if (arrel == null || arrel.dret == null) {
            return new AcbEnll<>(); // Árbol vacío
        }
        return new AcbEnll<>((NodeA) arrel.dret.clone());
    }

    @Override
    public boolean arbreBuit() {
        return arrel == null;
    }

    @Override
    public void buidar() {
        arrel = null; // Elimina todos los nodos del árbol
    }

    @Override
    public void inserir(E element) throws ArbreException {
        if (arrel == null) {
            arrel = new NodeA(element);
        } else {
            arrel.inserirRecursive(element);
        }
    }

    @Override
    public void esborrar(E element) throws ArbreException {
        if (arrel == null) {
            throw new ArbreException("Arbre buit");
        }
        arrel = arrel.esborrarRecursive(element);
    }

    @Override
    public boolean membre(E element) {
        if (arrel == null) {
            return false;
        }
        return arrel.membreRecursive(element);
    }

    public Queue<E> getAscendentList() {
        Queue<E> cua = new java.util.LinkedList<>();
        if (arrel != null) {
            arrel.omplirInOrdre(cua);
        }
        return cua;
    }

    public Queue<E> getDescendentList() {
        Queue<E> cua = new java.util.LinkedList<>();
        if (arrel != null) {
            arrel.omplirReverseInOrdre(cua);
        }
        return cua;
    }

    public int cardinalitat() {
        if (arrel == null) {
            return 0;
        }
        return arrel.cardinalitatRecursive();
    }
}
