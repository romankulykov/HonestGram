package moran_company.honestgram.eventbus;

/**
 * Created by roman on 25.01.2018.
 */

public class UpdateNavigation {

    private boolean foo;

    public UpdateNavigation(boolean foo) {
        this.foo = foo;
    }

    public boolean isFoo() {
        return foo;
    }

    public void setFoo(boolean foo) {
        this.foo = foo;
    }
}
