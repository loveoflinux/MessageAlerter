
package com.messagealerter.service;

import com.messagealerter.data.TwoDByteArray;

interface IAlerter {
	
	void feedPDUs(in TwoDByteArray pdus);
	void acknowledge(int alert);

}
