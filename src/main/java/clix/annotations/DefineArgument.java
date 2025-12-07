package clix.annotations;

public @interface DefineArgument {
    String name() default "";
    String description() default "";
    boolean reserved() default false;
}
