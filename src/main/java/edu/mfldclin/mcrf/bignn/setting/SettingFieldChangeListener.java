package edu.mfldclin.mcrf.bignn.setting;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SettingFieldChangeListener implements DocumentListener {

    private final SettingFieldChangeAction action;

    public SettingFieldChangeListener(SettingFieldChangeAction action) {
        this.action = action;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateValue(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateValue(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateValue(e);
    }

    private void updateValue(DocumentEvent e) {
        action.onChangeAction(e);
    }

}
