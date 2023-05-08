package com.omniscient.omnibalance.Core.Command.Parameters;


import com.omniscient.omnibalance.Core.Command.OmniParameter;

import java.util.List;
import java.util.function.Supplier;

public class IntegerParameter extends OmniParameter<Integer> {

    public IntegerParameter(String name, String permission, boolean required, Supplier<List<Integer>> values) {
        super(name, permission, required, values);
    }

    @Override
    public String stringify(Integer value) {
        return String.valueOf(value);
    }

    @Override
    public Integer typify(String s) throws OmniParameter.ParameterParseException {
        try{
            return Integer.valueOf(s);
        }catch (Exception e){
            throw new ParameterParseException(this, "must be an integer");
        }
    }
}
