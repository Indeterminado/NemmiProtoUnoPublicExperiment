* Encoding: UTF-8.
* Encoding: .
OUTPUT CLOSE ALL.
SET PRINTBACK=OFF.

DATASET ACTIVATE DataSet1.

TITLE Participant age and gender statistics.

EXAMINE VARIABLES=Age BY Profile
  /PLOT BOXPLOT STEMLEAF HISTOGRAM
  /COMPARE GROUPS
  /STATISTICS DESCRIPTIVES
  /CINTERVAL 95
  /MISSING LISTWISE
  /NOTOTAL.


NPAR TESTS
  /M-W= Age BY Profile(1 2)
  /MISSING ANALYSIS.

*** Gender by profile

CROSSTABS
  /TABLES=Gender BY Profile
  /FORMAT=AVALUE TABLES
  /STATISTICS=CHISQ 
  /CELLS=COUNT
  /COUNT ROUND CELL
  /BARCHART.

************************************************************************************************************************************
    * Phase 1 data analysis - uncategorized
    
TITLE Phase 1 data analysis - uncategorized.

**************************************
    * Phase 1 Stimulus A

CROSSTABS
    /TABLES=A_PIT_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.

CROSSTABS
    /TABLES= A_MAP_1_L1, A_RSN_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************
    * Phase 1 Stimulus B

CROSSTABS
    /TABLES=B_DUR_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.

CROSSTABS
    /TABLES=B_MAP_1_L1, B_RSN_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************
    * Phase 1 Stimulus C

CROSSTABS
    /TABLES=C_AMP_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.

CROSSTABS
    /TABLES=C_MAP_1_L1, C_RSN_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 1 Stimulus D

* Parameter perception

CROSSTABS
    /TABLES=D_PIT_1,  D_DUR_1, D_AMP_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.

* Mappings and reasons

CROSSTABS
    /TABLES=D_PITMAP_1_L1, D_DURMAP_1_L1,  D_AMPMAP_1_L1, D_RSN_1  BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 1 Stimulus E

CROSSTABS
    /TABLES=E_PAR_1, E_MAP_1_L1, E_RSN_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

************************************************************************************************************************************

* Compare variation within phase 1

TITLE Compare gesture choice variation within phase 1.

SUBTITLE Stimulus A-D: Pitch.

DO IF missing(A_MAP_1_L1) or missing(D_PITMAP_1_L1).
    COMPUTE ChangedPitch = (missing(A_MAP_1_L1) and missing(D_PITMAP_1_L1)).
ELSE.
    COMPUTE ChangedPitch = (A_MAP_1_L1 = D_PITMAP_1_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedPitch 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedPitch BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus B-D: Duration.

DO IF missing(B_MAP_1_L1) or missing(D_DURMAP_1_L1).
    COMPUTE ChangedDuration = (missing(B_MAP_1_L1) and missing(D_DURMAP_1_L1)).
ELSE.
    COMPUTE ChangedDuration = (B_MAP_1_L1 =D_DURMAP_1_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedDuration 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedDuration BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus C-D: Amplitude.

DO IF missing(C_MAP_1_L1) or missing(D_AMPMAP_1_L1).
    COMPUTE ChangedAmplitude = (missing(C_MAP_1_L1) and missing(D_AMPMAP_1_L1)).
ELSE.
    COMPUTE ChangedAmplitude = (C_MAP_1_L1 =D_AMPMAP_1_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedAmplitude 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedAmplitude BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

************************************************************************************************************************************

* Phase 2 data analysis - uncategorized
    
TITLE Phase 2 data analysis - uncategorized.

**************************************

* Phase 2 Stimulus A

CROSSTABS
    /TABLES=A_PIT_2, A_MAP_2_L1, A_RSN_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus B

CROSSTABS
    /TABLES=B_DUR_2, B_MAP_2_L1, B_RSN_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************


* Phase 2 Stimulus C

CROSSTABS
    /TABLES=C_AMP_2, C_MAP_2_L1, C_RSN_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus D

* Parameter perception

CROSSTABS
    /TABLES=D_PIT_2,  D_DUR_2, D_AMP_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

* Mappings and reasons

CROSSTABS
    /TABLES=D_PITMAP_2_L1, D_DURMAP_2_L1, D_AMPMAP_2_L1, D_RSN_2  BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus E

CROSSTABS
    /TABLES=E_PAR_2, E_MAP_2_L1, E_RSN_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

************************************************************************************************************************************

* Compare variation within phase 2

TITLE Compare gesture choice variation within phase 2.

SUBTITLE Stimulus A-D: Pitch.

DO IF missing(A_MAP_2_L1) or missing(D_PITMAP_2_L1).
    COMPUTE ChangedPitch = (missing(A_MAP_2_L1) and missing(D_PITMAP_2_L1)).
ELSE.
    COMPUTE ChangedPitch = (A_MAP_2_L1 =  D_PITMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedPitch 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedPitch BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus B-D: Duration.

DO IF missing(B_MAP_2_L1) or missing(D_DURMAP_2_L1).
    COMPUTE ChangedDuration = (missing(B_MAP_2_L1) and missing(D_DURMAP_2_L1)).
ELSE.
    COMPUTE ChangedDuration = (B_MAP_2_L1 =D_DURMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedDuration 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedDuration BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus C-D: Amplitude.

DO IF missing(C_MAP_2_L1) or missing(D_AMPMAP_2_L1).
    COMPUTE ChangedAmplitude = (missing(C_MAP_2_L1) and missing(D_AMPMAP_2_L1)).
ELSE.
    COMPUTE ChangedAmplitude = (C_MAP_2_L1 =D_AMPMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedAmplitude 0 'Changed mapping' 1 'Unchanged mapping'.

CROSSTABS
    /TABLES= ChangedAmplitude BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

************************************************************************************************************************************

* Compare variation between phases 1 and 2

* Stimulus A

TITLE Mapping inter-phase changes (Phase 1 to Phase 2).

SUBTITLE Stimulus A - Pitch.

DO IF missing(A_MAP_1_L1) or missing(A_MAP_2_L1).
    COMPUTE ChangedPitch = (missing(A_MAP_1_L1) and missing(A_MAP_2_L1)).
ELSE.
    COMPUTE ChangedPitch = (A_MAP_1_L1 =A_MAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedPitch 0 'Changed pitch mapping' 1 'Unchanged pitch mapping'.

CROSSTABS
    /TABLES= ChangedPitch BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

* Stimulus B

SUBTITLE Stimulus B - Duration.

DO IF missing(B_MAP_1_L1) or missing(B_MAP_2_L1).
    COMPUTE ChangedDuration = (missing(B_MAP_1_L1) and missing(B_MAP_2_L1)).
ELSE.
    COMPUTE ChangedDuration = (B_MAP_1_L1 =B_MAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedDuration 0 'Changed duration mapping' 1 'Unchanged duration mapping'.

CROSSTABS
    /TABLES= ChangedDuration BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

* Stimulus C

SUBTITLE Stimulus C - Amplitude.

DO IF missing(C_MAP_1_L1) or missing(C_MAP_2_L1).
    COMPUTE ChangedAmplitude = (missing(C_MAP_1_L1) and missing(C_MAP_2_L1)).
ELSE.
    COMPUTE ChangedAmplitude = (C_MAP_1_L1 =C_MAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedAmplitude 0 'Changed amplitude mapping' 1 'Unchanged amplitude mapping'.

CROSSTABS
    /TABLES= ChangedAmplitude BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

* Stimulus D

SUBTITLE Stimulus D - Pitch.

DO IF missing(D_PITMAP_1_L1) or missing(D_PITMAP_2_L1).
    COMPUTE ChangedPitch = (missing(D_PITMAP_1_L1) and missing(D_PITMAP_2_L1)).
ELSE.
    COMPUTE ChangedPitch = (D_PITMAP_1_L1 =D_PITMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedPitch 0 'Changed pitch mapping' 1 'Unchanged pitch mapping'.

CROSSTABS
    /TABLES= ChangedPitch BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus D - Duration.

DO IF missing(D_DURMAP_1_L1) or missing(D_DURMAP_2_L1).
    COMPUTE ChangedDuration = (missing(D_DURMAP_1_L1) and missing(D_DURMAP_2_L1)).
ELSE.
    COMPUTE ChangedDuration = (D_DURMAP_1_L1 =D_DURMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedDuration 0 'Changed pitch mapping' 1 'Unchanged pitch mapping'.

CROSSTABS
    /TABLES= ChangedDuration BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

SUBTITLE Stimulus D - Amplitude.

DO IF missing(D_AMPMAP_1_L1) or missing(D_AMPMAP_2_L1).
    COMPUTE ChangedAmplitude = (missing(D_AMPMAP_1_L1) and missing(D_AMPMAP_2_L1)).
ELSE.
    COMPUTE ChangedAmplitude = (D_AMPMAP_1_L1 =D_AMPMAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedAmplitude 0 'Changed pitch mapping' 1 'Unchanged pitch mapping'.

CROSSTABS
    /TABLES= ChangedAmplitude BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

* Stimulus E

SUBTITLE Stimulus E - Polyphony.

DO IF missing(E_MAP_1_L1) or missing(E_MAP_2_L1).
    COMPUTE ChangedPolyphony = (missing(E_MAP_1_L1) and missing(E_MAP_2_L1)).
ELSE.
    COMPUTE ChangedPolyphony = (E_MAP_1_L1 =E_MAP_2_L1).
END IF.
EXECUTE.

VALUE LABELS ChangedPolyphony 0 'Changed polyphony mapping' 1 'Unchanged polyphony mapping'.

CROSSTABS
    /TABLES= ChangedPolyphony BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.


************************************************************************************************************************************

* Phase 1 data analysis - categorized
    
TITLE Phase 1 data analysis - categorized.

**************************************
    * Phase 1 Stimulus A

CROSSTABS
    /TABLES= A_MAP_1_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.

**************************************

* Phase 1 Stimulus B

CROSSTABS
    /TABLES= B_MAP_1_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.

**************************************

* Phase 1 Stimulus C

CROSSTABS
    /TABLES= C_MAP_1_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.

**************************************
    * Phase 1 Stimulus D

CROSSTABS
    /TABLES= D_PITMAP_1_L2, D_DURMAP_1_L2, D_AMPMAP_1_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.

**************************************

* Phase 1 Stimulus E

CROSSTABS
    /TABLES= E_MAP_1_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS= COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.


************************************************************************************************************************************

* Phase 2 data analysis - categorized
    
TITLE Phase 2 data analysis - categorized.

**************************************

* Phase 2 Stimulus A

CROSSTABS
    /TABLES= A_MAP_2_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus B

CROSSTABS
    /TABLES= B_MAP_2_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus C

CROSSTABS
    /TABLES= C_MAP_2_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL.

**************************************

* Phase 2 Stimulus D

CROSSTABS
    /TABLES= D_PITMAP_2_L2, D_DURMAP_2_L2, D_AMPMAP_2_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS=COUNT COLUMN
    /COUNT ROUND CELL
    /BARCHART.

**************************************

* Phase 2 Stimulus E

CROSSTABS
    /TABLES= E_MAP_2_L2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS=CHISQ
    /CELLS= COUNT COLUMN
    /COUNT ROUND CELL.


************************************************************************************************************************************

* Infered note gestures
    
TITLE Infered note trigger gestures.

CROSSTABS
    /TABLES=INF_NT_1 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS= CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.

CROSSTABS
    /TABLES=INF_NT_2 BY Profile
    /FORMAT=AVALUE TABLES
    /STATISTICS= CHISQ
    /CELLS=COUNT
    /COUNT ROUND CELL.


************************************************************************************************************************************

* Modify output formats



OUTPUT MODIFY
    /REPORT PRINTREPORT=NO
    /SELECT TABLES
    /IF COMMANDS=["Crosstabs"] SUBTYPES=["Crosstabulation"]
    /TABLE PIVOT=[R1,C1].

OUTPUT MODIFY
    /REPORT PRINTREPORT=NO
    /SELECT TABLES
    /IF COMMANDS=["Crosstabs"] SUBTYPES=["Crosstabulation"]
    /TABLECELLS SELECT=[PERCENT] APPLYTO=COLUMNHEADER REPLACE="%"
    /TABLECELLS SELECT=[COUNT] APPLYTO=COLUMNHEADER REPLACE="N".

OUTPUT MODIFY
    /REPORT PRINTREPORT=NO
    /SELECT TABLES
    /IF SUBTYPES=["case processing summary" "statistics"]
    /DELETEOBJECT DELETE = YES.

COMMENT BOOKMARK;LINE_NUM=254;ID=1.
