/*
 * Created on Feb 2, 2005
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.editor.codecompletion.revisited;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.pydev.editor.codecompletion.revisited.modules.AbstractModule;
import org.python.pydev.editor.codecompletion.revisited.modules.SourceModule;
import org.python.pydev.editor.codecompletion.revisited.visitors.Definition;
import org.python.pydev.plugin.nature.PythonNature;

/**
 * @author Fabio Zadrozny
 */
public class CompletionState {
    public String activationToken; 
    public int line;
    public int col;
    public PythonNature nature;
    public Map memory = new HashMap();
    public Map definitionMemory = new HashMap();
    public Map wildImportMemory = new HashMap();

    
    public boolean builtinsGotten=false;
    
    /**
     * @param line2
     * @param col2
     * @param token
     * @param qual
     * @param nature2
     */
    public CompletionState(int line2, int col2, String token, PythonNature nature2) {
        this.line = line2;
        this.col = col2;
        this.activationToken = token;
        this.nature = nature2;
    }
    
    public CompletionState(){
        
    }

    public CompletionState getCopy(){
        CompletionState state = new CompletionState();
        state.activationToken = activationToken;
        state.line = line;
        state.col = col;
        state.builtinsGotten = builtinsGotten;
        state.nature = nature;
        state.memory = memory;
        state.wildImportMemory = wildImportMemory;
        state.definitionMemory = definitionMemory;
        return state;
    }

    /**
     * @param module
     * @param base
     */
    public void checkWildImportInMemory(AbstractModule caller, AbstractModule wild) {
        List l;
        if (this.wildImportMemory.containsKey(caller)){
            l = (List) this.wildImportMemory.get(caller);
            if(l.contains(wild)){
                throw new CompletionRecursionException("Recursion found (caller: "+caller.getName()+", import: "+wild.getName()+" )");
            }
        }else{
            l = new ArrayList();
        }
        
        l.add(wild);
        wildImportMemory.put(caller, l);
        
    }
    
    /**
     * @param module
     * @param definition
     */
    public void checkDefinitionMemory(AbstractModule module, Definition definition) {
        List l;
        if (this.definitionMemory.containsKey(module)){
            l = (List) this.definitionMemory.get(module);
            if(l.contains(definition)){
                throw new CompletionRecursionException("Recursion found (token: "+definition+")");
            }
        }else{
            l = new ArrayList();
        }
        
        l.add(definition);
        definitionMemory.put(module, l);
            
    }
    /**
     * @param module
     * @param base
     */
    public void checkMemory(SourceModule module, String base) {
        List l;
        if (this.memory.containsKey(module)){
            l = (List) this.memory.get(module);
            if(l.contains(base)){
                throw new CompletionRecursionException("Recursion found (token: "+base+")");
            }
        }else{
            l = new ArrayList();
        }
        
        l.add(base);
        memory.put(module, l);
    }
    
}
