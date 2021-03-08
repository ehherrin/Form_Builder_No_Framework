package Java;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

public class ServiceRequest {
	//Input from the home form
	private String srNo;
	private String procName;
	private String custName;
	private String destCol;
	private String destProcPnt1;
	private String destProcPnt2;
	private String lineNo;
	private String deptName;
	private String reqType;
	private MultipartFile[] destPhotos;
	private Blob destPhoto1;
	private Blob destPhoto2;
	private Blob destPhoto3;
	private String ticketNo;
	//Input from the new form
	private String newDevSel;
	//Input from the relocation form
	private String reloColLoc;
	private String reloProcPt1;
	private String reloProcPt2;
	private String simProcPt;
	private String curStaTyp;
	private String nodeIdNum;
	private String cntlrQty;
	private MultipartFile[] reloPhotos;
	private Blob reloPhoto1;
	private Blob reloPhoto2;
	private Blob reloPhoto3;
	//Input from the inventory form
	private String invDevSel;
	private String modified;
	private String created;

	//Getters and setters for the home form
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	
	public String getProcName() {
		return procName;
	}
	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getCustName(){
		return custName;
	}
	public void setCustName(String custName){
		this.custName = custName;
	}
	
	public String getDestCol(){
		return destCol;
	}
	public void setDestCol(String destCol){
		this.destCol = destCol;
	}
	
	public String getDestProcPnt1(){
		return destProcPnt1;
	}
	public void setDestProcPnt1(String destProcPnt1){
		this.destProcPnt1 = destProcPnt1;
	}
		
	public String getDestProcPnt2(){
		return destProcPnt2;
	}
	public void setDestProcPnt2(String destProcPnt2){
		this.destProcPnt2 = destProcPnt2;
	}
	
	public String getLineNo(){
		return lineNo;
	}
	public void setLineNo(String lineNo){
		this.lineNo = lineNo;
	}
	
	public String getDeptName(){
		return deptName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	
	public String getReqType(){
		return reqType;
	}
	public void setReqType(String reqType){
		this.reqType = reqType;
	}

	public MultipartFile[] getDestPhotos(){
		return destPhotos;
	}
	/*
	 * This setter will get the array of images and set the individual
	 * destPhotos values 1, 2, and 3.
	 */
	public void setDestPhotos(MultipartFile[] destPhotos) throws SerialException, SQLException, IOException{
		this.destPhotos = destPhotos;
		ArrayList<Blob> photoBlobs = new ArrayList<Blob>( );
		for ( MultipartFile photo : destPhotos){
			photoBlobs.add(new SerialBlob(photo.getBytes()));
		}
		switch (photoBlobs.size()){
		case 0:
			break;
		case 1:
			setDestPhoto1(photoBlobs.get(0));
			break;
		case 2:
			setDestPhoto1(photoBlobs.get(0));
			setDestPhoto2(photoBlobs.get(1));
			break;
		case 3:
			setDestPhoto1(photoBlobs.get(0));
			setDestPhoto2(photoBlobs.get(1));
			setDestPhoto3(photoBlobs.get(2));
			break;
		}
	}
	
	public Blob getDestPhoto1(){
		return destPhoto1;
	}
	public void setDestPhoto1(Blob destPhoto1){
		this.destPhoto1 = destPhoto1;
	}
	
	public Blob getDestPhoto2(){
		return destPhoto2;
	}
	public void setDestPhoto2(Blob destPhoto2){
		this.destPhoto2 = destPhoto2;
	}
	
	public Blob getDestPhoto3(){
		return destPhoto3;
	}
	public void setDestPhoto3(Blob destPhoto3){
		this.destPhoto3 = destPhoto3;
	}
		
	public String getNewDevSel() {
		return newDevSel;
	}
	public void setNewDevSel(String newDevSel) {
		this.newDevSel = newDevSel;
	}
		
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	
	//Relocation form getters and setters
	public String getReloColLoc() {
		return reloColLoc;
	}
	public void setReloColLoc(String reloColLoc) {
		this.reloColLoc = reloColLoc;
	}
	
	public String getReloProcPt1() {
		return reloProcPt1;
	}
	public void setReloProcPt1(String reloProcPt1) {
		this.reloProcPt1 = reloProcPt1;
	}
	
	public String getReloProcPt2() {
		return reloProcPt2;
	}
	public void setReloProcPt2(String reloProcPt2) {
		this.reloProcPt2 = reloProcPt2;
	}
	
	public String getSimProcPt() {
		return simProcPt;
	}
	public void setSimProcPt(String simProcPt) {
		this.simProcPt = simProcPt;
	}
	
	public String getCurStaTyp() {
		return curStaTyp;
	}
	public void setCurStaTyp(String curStaTyp) {
		this.curStaTyp = curStaTyp;
	}
	
	public String getNodeIdNum() {
		return nodeIdNum;
	}
	public void setNodeIdNum(String nodeIdNum) {
		this.nodeIdNum = nodeIdNum;
	}
	
	public String getCntlrQty() {
		return cntlrQty;
	}
	public void setCntlrQty(String cntlrQty) {
		this.cntlrQty = cntlrQty;
	}
		
	public MultipartFile[] getReloPhotos(){
		return reloPhotos;
	}
	/*
	 * This setter will get the array of images and set the individual
	 * destPhotos values 1, 2, and 3.
	 */
	public void setReloPhotos(MultipartFile[] reloPhotos) throws SerialException, SQLException, IOException{
		this.reloPhotos = reloPhotos;
		ArrayList<Blob> photoBlobs = new ArrayList<Blob>( );
		for ( MultipartFile photo : reloPhotos){
			photoBlobs.add(new SerialBlob(photo.getBytes()));
		}
		switch (photoBlobs.size()){
		case 0:
			break;
		case 1:
			setReloPhoto1(photoBlobs.get(0));
			break;
		case 2:
			setReloPhoto1(photoBlobs.get(0));
			setReloPhoto2(photoBlobs.get(1));
			break;
		case 3:
			setReloPhoto1(photoBlobs.get(0));
			setReloPhoto2(photoBlobs.get(1));
			setReloPhoto3(photoBlobs.get(2));
			break;
		}
	}
	
	public Blob getReloPhoto1(){
		return reloPhoto1;
	}
	public void setReloPhoto1(Blob reloPhoto1){
		this.reloPhoto1 = reloPhoto1;
	}
	
	public Blob getReloPhoto2(){
		return reloPhoto2;
	}

	public void setReloPhoto2(Blob reloPhoto2){
		this.reloPhoto2 = reloPhoto2;
	}
	
	public Blob getReloPhoto3(){
		return reloPhoto3;
	}	
	public void setReloPhoto3(Blob reloPhoto3){
		this.reloPhoto3 = reloPhoto3;
	}
	
	//Inventory form getters and setters
	public String getInvDevSel() {
		return invDevSel;
	}
	public void setInvDevSel(String invDevSel) {
		this.invDevSel = invDevSel;
	}
		
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
}