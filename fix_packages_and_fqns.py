import os
import re

base_src = "/home/luis/Documents/Projects/Glasfaser/quarkbau-monolith/src/main/java"

mappings = {
    "Project": "project.core",
    "ProjectRepository": "project.core",
    "ProjectService": "project.core",
    "ProjectController": "project.core",
    "ProjectDTO": "project.core",
    "ProjectMapper": "project.core",
    "ProjectStatus": "project.core",
    "ProjectNodesController": "project.core",
    
    "Cluster": "project.cluster",
    "ClusterRepository": "project.cluster",
    "ClusterService": "project.cluster",
    "ClusterController": "project.cluster",
    "ClusterDTO": "project.cluster",
    "ClusterMapper": "project.cluster",
    "Sector": "project.cluster",
    
    "Segment": "segment.core",
    "SegmentRepository": "segment.core",
    "SegmentService": "segment.core",
    "SmartSegmentRecognitionService": "segment.core",
    "SegmentController": "segment.core",
    "SegmentDTO": "segment.core",
    "SegmentMapper": "segment.core",
    "NearestSegmentDTO": "segment.core",
    "GeometryPoint": "segment.core",
    
    "SegmentPermit": "segment.permit",
    "SegmentPermitRepository": "segment.permit",
    "PermitService": "segment.permit",
    "PermitController": "segment.permit",
    
    "WorkType": "segment.workflow",
    "WorkflowState": "segment.workflow",
    "ConstructionPhaseState": "segment.workflow",
    "ConstructionPhaseFactory": "segment.workflow",
    "SegmentWorfklowObserver": "segment.workflow",
    "WorkflowStateChangedEvent": "segment.workflow",
    "SurveyState": "segment.workflow",
    "PermittingState": "segment.workflow",
    "ExcavationState": "segment.workflow",
    "DuctInstallationState": "segment.workflow",
    "BackfillState": "segment.workflow",
    "TrenchingState": "segment.workflow",
    "DrillingState": "segment.workflow",
    "RestorationState": "segment.workflow",
    "FiberBlowingState": "segment.workflow",
    "SplicingState": "segment.workflow",
    "AsphaltState": "segment.workflow",
    "QaState": "segment.workflow",
    "DocumentationState": "segment.workflow",
    "WorkOrder": "segment.workflow",
    
    "Pop": "network.node",
    "Netzverteiler": "network.node",
    "Muffe": "network.node",
    "Huep": "network.node",
    "PopRepository": "network.node",
    "NetzverteilerRepository": "network.node",
    "MuffeRepository": "network.node",
    "HuepRepository": "network.node",
    "PopService": "network.node",
    "NetzverteilerService": "network.node",
    "MuffeService": "network.node",
    "HuepService": "network.node",
    "PopController": "network.node",
    "NetzverteilerController": "network.node",
    "MuffeController": "network.node",
    "HuepController": "network.node",
    "PopDTO": "network.node",
    "PopMapper": "network.node",
    "NetzverteilerDTO": "network.node",
    "NetzverteilerMapper": "network.node",
    "MuffeDTO": "network.node",
    "MuffeMapper": "network.node",
    "HuepDTO": "network.node",
    "HuepMapper": "network.node",
    "DropConnection": "network.node",
    "DropStatus": "network.node",
    "FiberBox": "network.node",
    
    "Rohrverband": "network.duct",
    "RohrverbandRepository": "network.duct",
    "RohrverbandService": "network.duct",
    "RohrverbandController": "network.duct",
    
    "Employee": "workforce.personnel",
    "InternalEmployee": "workforce.personnel",
    "ExternalEmployee": "workforce.personnel",
    "CompanyRole": "workforce.personnel",
    "EmployeeRepository": "workforce.personnel",
    "EmployeeService": "workforce.personnel",
    "EmployeeController": "workforce.personnel",
    "EmployeeDTO": "workforce.personnel",
    "EmployeeMapper": "workforce.personnel",
    
    "Crew": "workforce.team",
    "CrewRepository": "workforce.team",
    "CrewBusyException": "workforce.team",
    "CrewType": "workforce.team",
    
    "Organization": "workforce.contractor",
    "Subcontractor": "workforce.contractor",
    "OrganizationRepository": "workforce.contractor",
    "SubcontractorRepository": "workforce.contractor",
    "SubcontractorService": "workforce.contractor",
    "SubcontractorController": "workforce.contractor",
    "SubcontractorDTO": "workforce.contractor",
    "SubcontractorMapper": "workforce.contractor",
    "ContractType": "workforce.contractor",
    
    "UtilityLine": "environment.utility",
    "WaterPipe": "environment.utility",
    "GasPipe": "environment.utility",
    "ElectricityCable": "environment.utility",
    "UtilityLineRepository": "environment.utility",
    "UtilityLineService": "environment.utility",
    "UtilityLineController": "environment.utility",
    "UtilityLineDTO": "environment.utility",
    "UtilityLineMapper": "environment.utility",
    "WaterPipeDTO": "environment.utility",
    "GasPipeDTO": "environment.utility",
    "ElectricityCableDTO": "environment.utility",
    
    "Hazard": "environment.safety",
    "FencingPlan": "environment.safety",
    "FencingElement": "environment.safety",
    "HazardRepository": "environment.safety",
    "FencingPlanRepository": "environment.safety",
    "HazardService": "environment.safety",
    "HazardController": "environment.safety",
    "FencingPlanController": "environment.safety",
    "HazardType": "environment.safety",
    "HazardSeverityLevel": "environment.safety",
    "HazardStatus": "environment.safety",
    
    "GlobalExceptionHandler": "shared.exception",
    "InventoryIntegrationService": "shared.integration"
}

def process_file(filepath):
    # Fix package
    rel_path = os.path.relpath(os.path.dirname(filepath), base_src)
    correct_package = rel_path.replace(os.sep, '.')
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    content = re.sub(r'^package\s+[^;]+;', f'package {correct_package};', content, flags=re.MULTILINE)
    
    # Fix fully qualified names (FQN) inside the code, e.g. com.quarkbau.monolith.planning.model.Segment
    for class_name, subdomain in mappings.items():
        old_fqn = r'com\.quarkbau\.monolith\.planning\.[a-z\.]+\.' + class_name + r'\b'
        new_fqn = f"com.quarkbau.monolith.planning.{subdomain}.{class_name}"
        # We replace any old FQN matching this
        content = re.sub(old_fqn, new_fqn, content)
        
    if new_content := content:
        if new_content != original_content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Fixed packages and FQNs in {filepath}")

for root, dirs, files in os.walk(base_src):
    for f in files:
        if f.endswith('.java'):
            process_file(os.path.join(root, f))
