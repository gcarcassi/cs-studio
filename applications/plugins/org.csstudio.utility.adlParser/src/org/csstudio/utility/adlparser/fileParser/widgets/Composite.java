package org.csstudio.utility.adlparser.fileParser.widgets;

import java.util.ArrayList;

import org.csstudio.utility.adlparser.internationalization.Messages;
import org.csstudio.utility.adlparser.fileParser.FileLine;
import org.csstudio.utility.adlparser.fileParser.ADLWidget;
import org.csstudio.utility.adlparser.fileParser.WrongADLFormatException;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLChildren;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLDynamicAttribute;
import org.csstudio.utility.adlparser.fileParser.widgetParts.ADLObject;

public class Composite extends ADLAbstractWidget {
	private String _compositeFile = new String();
	private boolean _hasCompositeFile = false;
	private ArrayList<ADLWidget> _children;
	
	public Composite(ADLWidget adlWidget) {
		super(adlWidget);
		try {
			for (ADLWidget childWidget : adlWidget.getObjects()) {
	        	if (childWidget.getType().equals("object")){
	        		_adlObject = new ADLObject(childWidget);
	        		if (_adlObject != null){
	        			_hasObject = true;
	        		}
	        		
	        	}
	        	else if (childWidget.getType().equals("children")){
	        		ADLChildren adlChildren = new ADLChildren(childWidget);
	        		if (adlChildren != null){
	        			_children = adlChildren.getAdlChildrens();
	        		}
	        	}
	        	else if (childWidget.getType().equals("dynamic attribute")){
	        		_adlDynamicAttribute = new ADLDynamicAttribute(childWidget);
	        		if (_adlDynamicAttribute != null){
	        			_hasDynamicAttribute = true;
	        		}
	        	}
	        }
	        for (FileLine fileLine : adlWidget.getBody()) {
	            String obj = fileLine.getLine();
	            String[] row = obj.trim().split("="); //$NON-NLS-1$
	            if(row.length<2){
	                throw new WrongADLFormatException(Messages.Label_WrongADLFormatException_Parameter_Begin+obj+Messages.Label_WrongADLFormatException_Parameter_End);
	            }
	            if(FileLine.argEquals(row[0], "composite file")){ //$NON-NLS-1$
	                set_compositeFile(FileLine.getTrimmedValue(row[1]));
	                if (!(_compositeFile.equals(""))){
	                	_hasCompositeFile = false;
	                }
	            	_hasCompositeFile = true;
	            }
	        }
		}
		catch (WrongADLFormatException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param _compositeFile the _compositeFile to set
	 */
	private void set_compositeFile(String _compositeFile) {
		this._compositeFile = _compositeFile;
	}

	/**
	 * @return the _compositeFile
	 */
	public String get_compositeFile() {
		return _compositeFile;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasCompositeFile(){
		return _hasCompositeFile;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<ADLWidget> getChildren() {
		return _children;
	}
}
