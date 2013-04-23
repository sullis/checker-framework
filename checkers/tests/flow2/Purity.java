import checkers.util.test.*;

import java.util.*;
import checkers.quals.*;
import tests.util.*;
import dataflow.quals.Deterministic;
import dataflow.quals.Pure;
import dataflow.quals.SideEffectFree;
import dataflow.quals.Pure.Kind;

// various tests for the @Pure annotation
class Purity {
    
    String f1, f2, f3;
    String[] a;
    
    // class with a (potentially) non-pure constructor
    private static class NonPureClass {
    }
    
    // class with a pure constructor
    private static class PureClass {
        @Pure
        public PureClass() {
        }
    }
    
    // a method that is not pure (no annotation)
    void nonpure() {
    }
    
    @Pure String pure() {
        return "";
    }
    
    //:: warning: (purity.void.method)
    @Pure void t1() {
    }
    
    @Pure String t2() {
        return "";
    }
    
    @Pure String t3() {
      //:: error: (purity.not.deterministic.not.sideeffectfree.call)
      nonpure();
      return "";
    }
    
    @Pure String t4() {
        pure();
        return "";
    }
    
    @Pure int t5() {
        int i = 1;
        return i;
    }
    
    @Pure int t6() {
        int j = 0;
        for (int i = 0; i < 10; i++) {
            j = j - i;
        }
        return j;
    }
    
    @Pure String t7() {
        if (true) {
            return "a";
        }
        return "";
    }
    
    @Pure int t8() {
        return 1 - 2 / 3 * 2 % 2;
    }
    
    @Pure String t9() {
        return "b" + "a";
    }
    
    @Pure String t10() {
        //:: error: (purity.not.deterministic.not.sideeffectfree.assign.field)
        f1 = "";
        //:: error: (purity.not.deterministic.not.sideeffectfree.assign.field)
        f2 = "";
        return "";
    }
    
    @Pure String t11(Purity l) {
        //:: error: (purity.not.deterministic.not.sideeffectfree.assign.array)
        l.a[0] = "";
        return "";
    }
    
    @Pure String t12(String[] s) {
        //:: error: (purity.not.deterministic.not.sideeffectfree.assign.array)
        s[0] = "";
        return "";
    }
    
    @Pure String t13() {
        //:: error: (purity.not.deterministic.object.creation)
        PureClass p = new PureClass();
        return "";
    }
    
    @SideEffectFree String t13b() {
        PureClass p = new PureClass();
        return "";
    }
    
    @Deterministic String t13c() {
        //:: error: (purity.not.deterministic.object.creation)
        PureClass p = new PureClass();
        return "";
    }
    
    @Pure String t14() {
        String i = "";
        i = "a";
        return i;
    }
    
    @Pure String t15() {
        String[] s = new String[1];
        return s[0];
    }

    @Pure String t16() {
        try {
            int i = 1/0;
            //:: error: (purity.not.deterministic.catch)
        } catch (Throwable t) {
            // ..
        }
        return "";
    }
    
    @SideEffectFree String t16b() {
        try {
            int i = 1/0;
        } catch (Throwable t) {
            // ..
        }
        return "";
    }
    
    @Deterministic String t16c() {
        try {
            int i = 1/0;
            //:: error: (purity.not.deterministic.catch)
        } catch (Throwable t) {
            // ..
        }
        return "";
    }
    
    @Pure String t12() {
        //:: error: (purity.not.deterministic.not.sideeffectfree.non.pure.object.creation)
        NonPureClass p = new NonPureClass();
        return "";
    }
}
