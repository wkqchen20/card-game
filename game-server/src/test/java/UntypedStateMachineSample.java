import com.wkqchen20.game.common.command.ClientCommand;
import org.squirrelframework.foundation.fsm.Condition;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.annotation.*;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

@States({
        @State(name = "A", exitCallMethod = "exitStateA"),
        @State(name = "B", entryCallMethod = "entryStateB"),
        @State(name = "C", entryCallMethod = "entryStateC"),
})
@Transitions({
        @Transit(from = "A", to = "B", on = "m", callMethod = "fromAToB"),
        @Transit(from = "B", to = "B", on = "n", callMethod = "fromBToC", when = UntypedStateMachineSample.FromBToACondition.class),
        @Transit(from = "B", to = "C", on = "n", callMethod = "fromBToC", when = UntypedStateMachineSample.FromBToCCondition.class),
        @Transit(from = "C", to = "A", on = "t")
})
@StateMachineParameters(stateType = String.class, eventType = String.class, contextType = Integer.class)
public class UntypedStateMachineSample extends AbstractUntypedStateMachine {

    static UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(UntypedStateMachineSample.class);
    static UntypedStateMachine fsm = builder.newStateMachine("A");

    // No need to specify constructor anymore since 0.2.9
    // protected UntypedStateMachineSample(ImmutableUntypedState initialState,
    //  Map<Object, ImmutableUntypedState> states) {
    //    super(initialState, states);
    // }

    protected void fromAToB(String from, String to, String event, Integer context) {
        // transition action still type safe ...
        System.out.println(String.format("fromAToB,from:%s,clientCommand:%s,event:%s,data:%s", from, to, event, context));
    }

    protected void fromBToC(String from, String to, String event, Integer context) {
        // transition action still type safe ...
        System.out.println(String.format("fromBToC,from:%s,clientCommand:%s,event:%s,data:%s", from, to, event, context));
    }


    protected void exitStateA(String from, String to, String event, Integer context) {
        System.out.println(String.format("exitStateA,from:%s,clientCommand:%s,event:%s,data:%s", from, to, event, context));
    }

    protected void entryStateB(String from, String to, String event, Integer context) {
        System.out.println(String.format("entryStateB,from:%s,clientCommand:%s,event:%s,data:%s", from, to, event, context));
    }

    protected void entryStateC(String from, String to, String event, Integer context) {
        System.out.println(String.format("entryStateC,from:%s,clientCommand:%s,event:%s,data:%s", from, to, event, context));
    }

    public static class FromBToACondition implements org.squirrelframework.foundation.fsm.Condition<Integer> {

        @Override
        public boolean isSatisfied(Integer context) {
            return context == 1;
        }

        @Override
        public String name() {
            return "FromBToACondition";
        }
    }

    public static class FromBToCCondition implements org.squirrelframework.foundation.fsm.Condition<Integer> {

        @Override
        public boolean isSatisfied(Integer context) {
            return context != 1;
        }

        @Override
        public String name() {
            return "FromBToCCondition";
        }
    }

    public static void main(String[] args) {

        fsm.start();
        System.out.println("Current state is " + fsm.getCurrentState());
        fsm.fire("m", 10);
        fsm.fire("n", 1);
        System.out.println("Current state is " + fsm.getCurrentState());

    }
}
