package com.github.project_njust.ccf_manager.service.impl;

import com.github.project_njust.ccf_manager.SQLManager;
import com.github.project_njust.ccf_manager.UserType;
import com.github.project_njust.ccf_manager.model.ExamScore;
import com.github.project_njust.ccf_manager.model.User;
import com.github.project_njust.ccf_manager.service.IResponse;
import com.github.project_njust.ccf_manager.service.ISubmitData;
import com.github.project_njust.ccf_manager.service.Service;
import com.github.project_njust.ccf_manager.wrapper.json.JsonSection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GetScore extends Service {
    public GetScore() {
        super("getScore", UserType.STUDENT);
    }

    @Override
    public @NotNull IResponse onRequest(@NotNull ISubmitData input) {
        User us = input.getUser();
        Integer uid = us.getUid();
        if (uid == null) {
            IResponse res = IResponse.createIResponse(IResponse.Status.ERROR);
            res.set("reason", "系统异常");
            return res;
        }
        List<ExamScore> exam = SQLManager.getExamScoreManager().selectExamScoreByUser(uid);

        if (exam == null) {
            IResponse res = IResponse.createIResponse(IResponse.Status.ERROR);
            res.set("reason", "未参加过考试");
            return res;
        } else {
            List<JsonSection> result = new ArrayList<>();
            for (ExamScore es : exam) {
                JsonSection js = JsonSection.createSection();
                js.set("examId", es.getExamid());
                js.set("examGrade", es.getExamgrade());
                result.add(js);
                System.out.println("examScores: " + js);
            }
            IResponse res = IResponse.createIResponse(IResponse.Status.SUCCESS);
            res.set("allScores", result);
            return res;
        }
    }
}
