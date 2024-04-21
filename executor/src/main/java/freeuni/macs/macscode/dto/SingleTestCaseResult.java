package freeuni.macs.macscode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SingleTestCaseResult {

    private Integer testNum;
    private String result;
    private String additionalInfo;

    public SingleTestCaseResult(Integer testNum, String result) {
        this.testNum = testNum;
        this.result = result;
    }

}
