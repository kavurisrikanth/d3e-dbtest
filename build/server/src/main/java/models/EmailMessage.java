package models;

import d3e.core.CloneContext;
import d3e.core.DFile;
import d3e.core.SchemaConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.solr.client.solrj.beans.Field;
import store.DatabaseObject;
import store.ICloneable;

public class EmailMessage extends D3EMessage {
  public static final int _BCC = 4;
  public static final int _CC = 5;
  public static final int _SUBJECT = 6;
  public static final int _HTML = 7;
  public static final int _INLINEATTACHMENTS = 8;
  public static final int _ATTACHMENTS = 9;
  @Field private List<String> bcc = new ArrayList<>();
  @Field private List<String> cc = new ArrayList<>();
  @Field private String subject;
  @Field private boolean html = false;
  @Field private List<DFile> inlineAttachments = new ArrayList<>();
  @Field private List<DFile> attachments = new ArrayList<>();

  @Override
  public int _typeIdx() {
    return SchemaConstants.EmailMessage;
  }

  @Override
  public String _type() {
    return "EmailMessage";
  }

  @Override
  public int _fieldsCount() {
    return 10;
  }

  public void addToBcc(String val, long index) {
    collFieldChanged(_BCC, this.bcc);
    if (index == -1) {
      this.bcc.add(val);
    } else {
      this.bcc.add(((int) index), val);
    }
  }

  public void removeFromBcc(String val) {
    collFieldChanged(_BCC, this.bcc);
    this.bcc.remove(val);
  }

  public void addToCc(String val, long index) {
    collFieldChanged(_CC, this.cc);
    if (index == -1) {
      this.cc.add(val);
    } else {
      this.cc.add(((int) index), val);
    }
  }

  public void removeFromCc(String val) {
    collFieldChanged(_CC, this.cc);
    this.cc.remove(val);
  }

  public void addToInlineAttachments(DFile val, long index) {
    collFieldChanged(_INLINEATTACHMENTS, this.inlineAttachments);
    if (index == -1) {
      this.inlineAttachments.add(val);
    } else {
      this.inlineAttachments.add(((int) index), val);
    }
  }

  public void removeFromInlineAttachments(DFile val) {
    collFieldChanged(_INLINEATTACHMENTS, this.inlineAttachments);
    this.inlineAttachments.remove(val);
  }

  public void addToAttachments(DFile val, long index) {
    collFieldChanged(_ATTACHMENTS, this.attachments);
    if (index == -1) {
      this.attachments.add(val);
    } else {
      this.attachments.add(((int) index), val);
    }
  }

  public void removeFromAttachments(DFile val) {
    collFieldChanged(_ATTACHMENTS, this.attachments);
    this.attachments.remove(val);
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public List<String> getBcc() {
    return this.bcc;
  }

  public void setBcc(List<String> bcc) {
    if (Objects.equals(this.bcc, bcc)) {
      return;
    }
    collFieldChanged(_BCC, this.bcc);
    this.bcc.clear();
    this.bcc.addAll(bcc);
  }

  public List<String> getCc() {
    return this.cc;
  }

  public void setCc(List<String> cc) {
    if (Objects.equals(this.cc, cc)) {
      return;
    }
    collFieldChanged(_CC, this.cc);
    this.cc.clear();
    this.cc.addAll(cc);
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    if (Objects.equals(this.subject, subject)) {
      return;
    }
    fieldChanged(_SUBJECT, this.subject);
    this.subject = subject;
  }

  public boolean isHtml() {
    return this.html;
  }

  public void setHtml(boolean html) {
    if (Objects.equals(this.html, html)) {
      return;
    }
    fieldChanged(_HTML, this.html);
    this.html = html;
  }

  public List<DFile> getInlineAttachments() {
    return this.inlineAttachments;
  }

  public void setInlineAttachments(List<DFile> inlineAttachments) {
    if (Objects.equals(this.inlineAttachments, inlineAttachments)) {
      return;
    }
    collFieldChanged(_INLINEATTACHMENTS, this.inlineAttachments);
    this.inlineAttachments.clear();
    this.inlineAttachments.addAll(inlineAttachments);
  }

  public List<DFile> getAttachments() {
    return this.attachments;
  }

  public void setAttachments(List<DFile> attachments) {
    if (Objects.equals(this.attachments, attachments)) {
      return;
    }
    collFieldChanged(_ATTACHMENTS, this.attachments);
    this.attachments.clear();
    this.attachments.addAll(attachments);
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof EmailMessage && super.equals(a);
  }

  public EmailMessage deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    EmailMessage _obj = ((EmailMessage) dbObj);
    _obj.setBcc(bcc);
    _obj.setCc(cc);
    _obj.setSubject(subject);
    _obj.setHtml(html);
    _obj.setInlineAttachments(inlineAttachments);
    _obj.setAttachments(attachments);
  }

  public EmailMessage cloneInstance(EmailMessage cloneObj) {
    if (cloneObj == null) {
      cloneObj = new EmailMessage();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setBcc(new ArrayList<>(this.getBcc()));
    cloneObj.setCc(new ArrayList<>(this.getCc()));
    cloneObj.setSubject(this.getSubject());
    cloneObj.setHtml(this.isHtml());
    cloneObj.setInlineAttachments(new ArrayList<>(this.getInlineAttachments()));
    cloneObj.setAttachments(new ArrayList<>(this.getAttachments()));
    return cloneObj;
  }

  public boolean transientModel() {
    return true;
  }

  public EmailMessage createNewInstance() {
    return new EmailMessage();
  }

  public void collectCreatableReferences(List<Object> _refs) {
    super.collectCreatableReferences(_refs);
    _refs.addAll(this.inlineAttachments);
    _refs.addAll(this.attachments);
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    super._handleChildChange(_childIdx);
  }
}
