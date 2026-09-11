/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : BasicTermDataComparator.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.util;

import java.util.Comparator;

import hk.org.ha.iams.termsearch.dao.vo.BasicTermDataVO;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.RequestSystem;

public class BasicTermDataComparator implements Comparator<BasicTermDataVO> {

	private boolean isKeywordSearch;
	private RequestSystem requestSystem;
	private CodeType sortingCodeType;

	public enum Nature {
		DX("Dx", "D"),
		PX("Px", "P"),
		ORGANISM("Organism", null),
		LAB_TEST("Lab. Test", null),
		ALERT("Alert", null);

		private final String iamsNature;
		private final String cdfNature;

		Nature(String iamsNature, String cdfNature) {
			this.iamsNature = iamsNature;
			this.cdfNature = cdfNature;
		}

		public String getIamsNature() {
			return iamsNature;
		}

		public String getCdfNature() {
			return cdfNature;
		}

		public String toString() {
			return iamsNature;
		}

		public static Nature getNatureByIamsNature(String iamsNature) {
			Nature correspondingNature = null;

			if (iamsNature != null) {
				for (Nature natureConstant : values()) {
					if (natureConstant.getIamsNature().equals(iamsNature)) {
						correspondingNature = natureConstant;
						break;
					}
				}
			}

			return correspondingNature;
		}

		public static Nature getNatureByCdfNature(String cdfNature) {
			Nature correspondingNature = null;

			if (cdfNature != null) {
				for (Nature natureConstant : values()) {
					if (natureConstant.getCdfNature() != null && natureConstant.getCdfNature().equals(cdfNature)) {
						correspondingNature = natureConstant;
						break;
					}
				}
			}

			return correspondingNature;
		}
	}

	public BasicTermDataComparator(boolean isKeywordSearch, RequestSystem inRequestSystem, CodeType sortingCodeType) {
		super();

		this.isKeywordSearch = isKeywordSearch;
		this.requestSystem = inRequestSystem;
		this.sortingCodeType = sortingCodeType;
	}

	public int compare(BasicTermDataVO obj1, BasicTermDataVO obj2) {
		int result = 0;

		if (obj1 != null && obj2 != null) {
			if (isKeywordSearch) {
				result = obj2.getSortingScore().compareTo(obj1.getSortingScore());

				if (result == 0) {
					// If equals scores

					// If both object are CDF overrided, sort by cdf description
					if (obj1.isCdfOverride() && obj2.isCdfOverride()) {
						result = obj1.getCdfDescription().compareTo(obj2.getCdfDescription());
					}
					else if (obj1.isCdfOverride()) {
						result = -1;
					}
					else if (obj2.isCdfOverride()) {
						result = 1;
					}

					if (!requestSystem.equals(RequestSystem.FM)) {

						// 2th check full description
						// Do this comparison only if Exact Match
						if (result == 0) {
							if (obj1.getExactMatch() == 1 ) {
								result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
							}
						}

						if (result == 0 && sortingCodeType != null) {
							// sort according to input code type
							result = sortCodeType(result, obj1, obj2);
						}

						// 3rd check nature
						if (result == 0) {
							result = obj1.getNature().compareTo(obj2.getNature());
						}

						// Move to (3.2) at 27 Sep 2012 
						// 4th check full description
						//if (result == 0) {
						//	result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
						//}

					}
					else { // FM
						// 2nd check icpc2 code
						if (result == 0 && (obj1.getIcpc2Code() != null || obj2.getIcpc2Code() != null)) {
							if (obj1.getIcpc2Code() != null && obj2.getIcpc2Code() != null) {
								result = obj1.getIcpc2Code().compareTo(obj2.getIcpc2Code());

							}
							else if (obj2.getIcpc2Code() != null) {
								result = 1;
							}
							else if (obj1.getIcpc2Code() != null) {
								result = -1;
							}
						}

						// 3.1 check exact match of full description
						// Do this comparison only if Exact Match
						if (result == 0) {
							if (obj1.getExactMatch() == 1 ) {
								result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
							}
						}

						// 3.2 check exact match of ailases
						// Do this comparison only if Exact Match
						if (result == 0) {
							if (obj1.getExactMatch() == 1 ) {
								result = obj1.getMatchedAliasCount().compareTo(obj2.getMatchedAliasCount());
							}
						}

						// 3.3 check exact match of IAMS short description 
						// Do this comparison only if Exact Match
						if (result == 0) {
							if (obj1.getExactMatch() == 1 ) {
								result = obj1.getShortDesc().compareTo(obj2.getShortDesc());
							}
						}

						// 4.1 Partial match of IAMS full description
						if (result == 0) {
							if (obj1.getExactMatch() == 0 ) {
								result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
							}
						}

						// 4.2 Partial match of IAMS alias
						if (result == 0) {
							if (obj1.getExactMatch() == 0 ) {
								result = obj1.getMatchedAliasCount().compareTo(obj2.getMatchedAliasCount());
							}
						}

						// 4.3 Partial match of IAMS short description 
						if (result == 0) {
							if (obj1.getExactMatch() == 0 ) {
								result = obj1.getShortDesc().compareTo(obj2.getShortDesc());
							}
						}

						// 5th If both object are not CDF overrided or equals CDF description, sort by ICD code
						// 5.1 sort by ICD9Dx
						if (result == 0 && (obj1.getIcd9dxCode() != null || obj2.getIcd9dxCode() != null)) {
							if (obj1.getIcd9dxCode() != null && obj2.getIcd9dxCode() != null) {
								result = obj1.getIcd9dxCode().compareTo(obj2.getIcd9dxCode());

								if (result == 0) {
									if (obj1.getIcd9dxExtension() != null || obj2.getIcd9dxExtension() != null) {
										if (obj1.getIcd9dxExtension() != null && obj2.getIcd9dxExtension() != null) {

											//Although the ICD9 extension is smallint type, but we treat them as string to sort.
											result = obj1.getIcd9dxExtension().toString().compareTo(obj2.getIcd9dxExtension().toString());
										}
										else if (obj2.getIcd9dxExtension() != null) {
											result = 1;
										}
										else if (obj1.getIcd9dxExtension() != null) {
											result = -1;
										}
									}
								}
							}
							else if (obj2.getIcd9dxCode() != null) {
								result = 1;
							}
							else if (obj1.getIcd9dxCode() != null) {
								result = -1;
							}
						}

						// 5.2 check ICD9Px
						if (result == 0 && (obj1.getIcd9pxCode() != null || obj2.getIcd9pxCode() != null)) {
							if (obj1.getIcd9pxCode() != null && obj2.getIcd9pxCode() != null) {
								result = obj1.getIcd9pxCode().compareTo(obj2.getIcd9pxCode());

								if (result == 0) {
									if (obj1.getIcd9pxExtension() != null || obj2.getIcd9pxExtension() != null) {
										if (obj1.getIcd9pxExtension() != null && obj2.getIcd9pxExtension() != null) {

											//Although the ICD9 extension is smallint type, but we treat them as string to sort.
											result = obj1.getIcd9pxExtension().toString().compareTo(obj2.getIcd9pxExtension().toString());
										}
										else if (obj2.getIcd9pxExtension() != null) {
											result = 1;
										}
										else if (obj1.getIcd9pxExtension() != null) {
											result = -1;
										}
									}
								}
							}
							else if (obj2.getIcd9pxCode() != null) {
								result = 1;
							}
							else if (obj1.getIcd9pxCode() != null) {
								result = -1;
							}
						}

						// 6 check nature
						if (result == 0) {
							result = obj1.getNature().compareTo(obj2.getNature());
						}

					}
				}
			}
			else {  // if not KeywordSearch
				if (!requestSystem.equals(RequestSystem.FM)) {
					// 1st check nature
					Nature obj1Nature = null;
					Nature obj2Nature = null;

					if (obj1.isCdfOverride()) {
						obj1Nature = Nature.getNatureByCdfNature(obj1.getCdfNature());
					}
					else {
						obj1Nature = Nature.getNatureByIamsNature(obj1.getNature());
					}

					if (obj2.isCdfOverride()) {
						obj2Nature = Nature.getNatureByCdfNature(obj2.getCdfNature());
					}
					else {
						obj2Nature = Nature.getNatureByIamsNature(obj2.getNature());
					}

					if (obj1Nature != null || obj2Nature != null) {
						if (obj1Nature == null) {
							result = 1;
						}
						else if (obj2Nature == null) {
							result = -1;
						}
						else if (!obj1Nature.equals(obj2Nature)) {
							if (obj1Nature.equals(Nature.DX)) {
								result = -1;
							}
							else if (obj2Nature.equals(Nature.DX)) {
								result = 1;
							}
						}
					}

					// 2nd check clinical data id
					if (result == 0 && (obj1.getClinicalDataId() != null || obj2.getClinicalDataId() != null)) {
						if (obj1.getClinicalDataId() != null && obj2.getClinicalDataId() != null) {
							result = obj2.getClinicalDataId().compareTo(obj1.getClinicalDataId());
						}
						else if (obj2.getClinicalDataId() != null) {
							result = 1;
						}
						else if (obj1.getClinicalDataId() != null) {
							result = -1;
						}
					}

					if (result == 0 && sortingCodeType != null) {
						// sort according to input code type
						result = sortCodeType(result, obj1, obj2);
					}

					// 5th check nature
					if (result == 0) {
						result = obj1.getNature().compareTo(obj2.getNature());
					}
					// 6th check full description
					if (result == 0) {
						result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
					}
				}
				else { // FM not keywordSearch e.g. code search

					// check icpc2 code
					if (result == 0 && (obj1.getIcpc2Code() != null || obj2.getIcpc2Code() != null)) {
						if (obj1.getIcpc2Code() != null && obj2.getIcpc2Code() != null) {
							result = obj1.getIcpc2Code().compareTo(obj2.getIcpc2Code());

						}
						else if (obj2.getIcpc2Code() != null) {
							result = 1;
						}
						else if (obj1.getIcpc2Code() != null) {
							result = -1;
						}
					}

					// check full description
					if (result == 0) {
						result = obj1.getFullDesc().compareTo(obj2.getFullDesc());
					}

					// 1st check nature
					Nature obj1Nature = null;
					Nature obj2Nature = null;

					if (result == 0) {
						if (obj1.isCdfOverride()) {
							obj1Nature = Nature.getNatureByCdfNature(obj1.getCdfNature());
						}
						else {
							obj1Nature = Nature.getNatureByIamsNature(obj1.getNature());
						}

						if (obj2.isCdfOverride()) {
							obj2Nature = Nature.getNatureByCdfNature(obj2.getCdfNature());
						}
						else {
							obj2Nature = Nature.getNatureByIamsNature(obj2.getNature());
						}

						if (obj1Nature != null || obj2Nature != null) {
							if (obj1Nature == null) {
								result = 1;
							}
							else if (obj2Nature == null) {
								result = -1;
							}
							else if (!obj1Nature.equals(obj2Nature)) {
								if (obj1Nature.equals(Nature.DX)) {
									result = -1;
								}
								else if (obj2Nature.equals(Nature.DX)) {
									result = 1;
								}
							}
						}
					}

					// 2nd check clinical data id
					if (result == 0 && (obj1.getClinicalDataId() != null || obj2.getClinicalDataId() != null)) {
						if (obj1.getClinicalDataId() != null && obj2.getClinicalDataId() != null) {
							result = obj2.getClinicalDataId().compareTo(obj1.getClinicalDataId());
						}
						else if (obj2.getClinicalDataId() != null) {
							result = 1;
						}
						else if (obj1.getClinicalDataId() != null) {
							result = -1;
						}
					}
					// 3rd check ICD9Dx
					if (result == 0 && (obj1.getIcd9dxCode() != null || obj2.getIcd9dxCode() != null)) {
						if (obj1.getIcd9dxCode() != null && obj2.getIcd9dxCode() != null) {
							result = obj1.getIcd9dxCode().compareTo(obj2.getIcd9dxCode());

							if (result == 0) {
								if (obj1.getIcd9dxExtension() != null || obj2.getIcd9dxExtension() != null) {
									if (obj1.getIcd9dxExtension() != null && obj2.getIcd9dxExtension() != null) {
										result = obj1.getIcd9dxExtension().compareTo(obj2.getIcd9dxExtension());
									}
									else if (obj2.getIcd9dxExtension() != null) {
										result = 1;
									}
									else if (obj1.getIcd9dxExtension() != null) {
										result = -1;
									}
								}
							}
						}
						else if (obj2.getIcd9dxCode() != null) {
							result = 1;
						}
						else if (obj1.getIcd9dxCode() != null) {
							result = -1;
						}
					}
					// 4th check ICD9Px
					if (result == 0 && (obj1.getIcd9pxCode() != null || obj2.getIcd9pxCode() != null)) {
						if (obj1.getIcd9pxCode() != null && obj2.getIcd9pxCode() != null) {
							result = obj1.getIcd9pxCode().compareTo(obj2.getIcd9pxCode());

							if (result == 0) {
								if (obj1.getIcd9pxExtension() != null || obj2.getIcd9pxExtension() != null) {
									if (obj1.getIcd9pxExtension() != null && obj2.getIcd9pxExtension() != null) {
										result = obj1.getIcd9pxExtension().compareTo(obj2.getIcd9pxExtension());
									}
									else if (obj2.getIcd9pxExtension() != null) {
										result = 1;
									}
									else if (obj1.getIcd9pxExtension() != null) {
										result = -1;
									}
								}
							}
						}
						else if (obj2.getIcd9pxCode() != null) {
							result = 1;
						}
						else if (obj1.getIcd9pxCode() != null) {
							result = -1;
						}
					}
					// 5th check nature
					if (result == 0) {
						result = obj1.getNature().compareTo(obj2.getNature());
					}

				}
			}
		}
		else if (obj2 != null) {
			result = 1;
		}
		else if (obj1 != null) {
			result = -1;
		}

		return result;
	}

	// general sorting
	private int sortCodeType(int inResult, BasicTermDataVO obj1, BasicTermDataVO obj2) { 
		int sortResult = inResult;

		if (sortingCodeType.equals(CodeType.ICD9Dx)) {
			// If both object are not CDF overrided or equals CDF description, sort by ICD code 
			// 1st sort by ICD9Dx
			if (sortResult == 0 && (obj1.getIcd9dxCode() != null || obj2.getIcd9dxCode() != null)) {
				if (obj1.getIcd9dxCode() != null && obj2.getIcd9dxCode() != null) {
					sortResult = obj1.getIcd9dxCode().compareTo(obj2.getIcd9dxCode());

					if (sortResult == 0) {
						if (obj1.getIcd9dxExtension() != null || obj2.getIcd9dxExtension() != null) {
							if (obj1.getIcd9dxExtension() != null && obj2.getIcd9dxExtension() != null) {

								//Although the ICD9 extension is smallint type, but we treat them as string to sort.
								sortResult = obj1.getIcd9dxExtension().toString().compareTo(obj2.getIcd9dxExtension().toString());
							}
							else if (obj2.getIcd9dxExtension() != null) {
								sortResult = 1;
							}
							else if (obj1.getIcd9dxExtension() != null) {
								sortResult = -1;
							}
						}
					}
				}
				else if (obj2.getIcd9dxCode() != null) {
					sortResult = 1;
				}
				else if (obj1.getIcd9dxCode() != null) {
					sortResult = -1;
				}
			}

			// 2nd check ICD9Px  (PWH special handling)
			if (sortResult == 0 && (obj1.getIcd9pxCode() != null || obj2.getIcd9pxCode() != null)) {
				if (obj1.getIcd9pxCode() != null && obj2.getIcd9pxCode() != null) {
					sortResult = obj1.getIcd9pxCode().compareTo(obj2.getIcd9pxCode());

					if (sortResult == 0) {
						if (obj1.getIcd9pxExtension() != null || obj2.getIcd9pxExtension() != null) {
							if (obj1.getIcd9pxExtension() != null && obj2.getIcd9pxExtension() != null) {

								//Although the ICD9 extension is smallint type, but we treat them as string to sort.
								sortResult = obj1.getIcd9pxExtension().toString().compareTo(obj2.getIcd9pxExtension().toString());
							}
							else if (obj2.getIcd9pxExtension() != null) {
								sortResult = 1;
							}
							else if (obj1.getIcd9pxExtension() != null) {
								sortResult = -1;
							}
						}
					}
				}
				else if (obj2.getIcd9pxCode() != null) {
					sortResult = 1;
				}
				else if (obj1.getIcd9pxCode() != null) {
					sortResult = -1;
				}
			}
		}

		else if (sortingCodeType.equals(CodeType.ICD9Px)) {
			// check ICD9Px
			if (sortResult == 0 && (obj1.getIcd9pxCode() != null || obj2.getIcd9pxCode() != null)) {
				if (obj1.getIcd9pxCode() != null && obj2.getIcd9pxCode() != null) {
					sortResult = obj1.getIcd9pxCode().compareTo(obj2.getIcd9pxCode());

					if (sortResult == 0) {
						if (obj1.getIcd9pxExtension() != null || obj2.getIcd9pxExtension() != null) {
							if (obj1.getIcd9pxExtension() != null && obj2.getIcd9pxExtension() != null) {

								//Although the ICD9 extension is smallint type, but we treat them as string to sort.
								sortResult = obj1.getIcd9pxExtension().toString().compareTo(obj2.getIcd9pxExtension().toString());
							}
							else if (obj2.getIcd9pxExtension() != null) {
								sortResult = 1;
							}
							else if (obj1.getIcd9pxExtension() != null) {
								sortResult = -1;
							}
						}
					}
				}
				else if (obj2.getIcd9pxCode() != null) {
					sortResult = 1;
				}
				else if (obj1.getIcd9pxCode() != null) {
					sortResult = -1;
				}
			}
		} 

		else if (sortingCodeType.equals(CodeType.ICD102010MBD)) {
			// check ICD10-2010+MBD
			if (sortResult == 0 && (obj1.getIcd102010MBDCode() != null || obj2.getIcd102010MBDCode() != null)) {
				if (obj1.getIcd102010MBDCode() != null && obj2.getIcd102010MBDCode() != null) {
					sortResult = obj1.getIcd102010MBDCode().compareTo(obj2.getIcd102010MBDCode());

				}
				else if (obj2.getIcd102010MBDCode() != null) {
					sortResult = 1;
				}
				else if (obj1.getIcd102010MBDCode() != null) {
					sortResult = -1;
				}
			}
		}

		else if (sortingCodeType.equals(CodeType.ICPC2)) {
			// check ICPC2
			if (sortResult == 0 && (obj1.getIcpc2Code() != null || obj2.getIcpc2Code() != null)) {
				if (obj1.getIcpc2Code() != null && obj2.getIcpc2Code() != null) {
					sortResult = obj1.getIcpc2Code().compareTo(obj2.getIcpc2Code());

				}
				else if (obj2.getIcpc2Code() != null) {
					sortResult = 1;
				}
				else if (obj1.getIcpc2Code() != null) {
					sortResult = -1;
				}
			}
		}

		return sortResult;
	}
}
